package it.polimi.streamreasoning.xbench.writers;

import java.util.Stack;

import it.polimi.streamreasoning.xbench.generators.GeneratorCallbackTarget;
import it.polimi.streamreasoning.xbench.state.GlobalState;
import it.polimi.streamreasoning.xbench.model.Ontology;

public abstract class FlatWriter extends AbstractWriter implements Writer {

    protected static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
    protected static final String OWL_ONTOLOGY = "http://www.w3.org/2002/07/owl#Ontology";
    protected static final String OWL_IMPORTS = "http://www.w3.org/2002/07/owl#imports";
    
    protected final String ontologyUrl;
    private final Stack<String> subjects = new Stack<String>();

    public FlatWriter(GeneratorCallbackTarget target, String ontologyUrl) {
        super(target);
        this.ontologyUrl = ontologyUrl;
    }

    @Override
    public void startFile(String fileName, GlobalState state) {
        prepareOutputStream(fileName, state);
        addOntologyDeclaration();
    }

    protected void addOntologyDeclaration() {
        // Add Ontology declaration
        this.subjects.push("");
        this.addTriple(RDF_TYPE, OWL_ONTOLOGY, true);
        this.addTriple(OWL_IMPORTS, this.ontologyUrl, true);
        this.subjects.pop();
    }

    @Override
    public void endFile(GlobalState state) {
        if (!subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in endFile()");
        cleanupOutputStream();

    }

    protected String getCurrentSubject() {
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in getCurrentSubject()");
        return this.subjects.peek();
    }

    protected abstract void addTriple(String property, String object, boolean isResource);

    protected abstract void addTypeTriple(String subject, int classType);

    @Override
    public final void startSection(int classType, String id) {
        callbackTarget.startSectionCB(classType);
        newSection(classType, id);
    }

    protected void newSection(int classType, String id) {
        if (!this.subjects.isEmpty()) {
            // Nested section which we don't support directly
            // Link the existing subject to the new subject
            addTriple(this.getCurrentSubject(), id, true);
            this.subjects.push(id);

            // Add type triple
            addTypeTriple(id, classType);
        } else {
            // Top level section
            this.subjects.push(id);
            addTypeTriple(id, classType);
        }
    }

    @Override
    public final void startAboutSection(int classType, String id) {
        callbackTarget.startAboutSectionCB(classType);
        newSection(classType, id);
    }

    @Override
    public final void endSection(int classType) {
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in endSection()");
        this.subjects.pop();
    }

    @Override
    public final void addProperty(int property, String value, boolean isResource) {
        callbackTarget.addPropertyCB(property);
        
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in addProperty()");

        String propertyUrl = String.format("%s#%s", this.ontologyUrl, Ontology.PROP_TOKEN[property]);
        addTriple(propertyUrl, value, isResource);
    }

    @Override
    public final void addProperty(int property, int valueClass, String valueId) {
        callbackTarget.addPropertyCB(property);
        callbackTarget.addValueClassCB(valueClass);
        
        if (this.subjects.isEmpty())
            throw new RuntimeException("Mismatched calls to writer in addTypedProperty()");

        addProperty(property, valueId, true);
        addTypeTriple(valueId, valueClass);
    }

}
