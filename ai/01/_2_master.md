
In this chapter, we will adopt the formal framework of `"generative grammar"`, in which a `"language"` is **considered to be nothing more than an enormous collection of all grammatical sentences**, and a `grammar` is a **formal notation that can be used for `"generating"` the members of this set**. Grammars use recursive productions of the form S → S and S, as we will explore in Section 8.3. In Chapter 10 we will extend this, to automatically build up the meaning of a sentence out of the meanings of its parts.

Ubiquitous Ambiguity

A well-known example of ambiguity is shown in (2), from the Groucho Marx movie, Animal Crackers (1930):

(2)		While hunting in Africa, I shot an elephant in my pajamas. How an elephant got into my pajamas I'll never know.
Let's take a closer look at the ambiguity in the phrase: I shot an elephant in my pajamas. First we need to define a simple grammar:


>>> groucho_grammar = nltk.parse_cfg("""
... S -> NP VP
... PP -> P NP
... NP -> Det N | Det N PP | 'I'
... VP -> V NP | VP PP
... Det -> 'an' | 'my'
... N -> 'elephant' | 'pajamas'
... V -> 'shot'
... P -> 'in'
... """)


