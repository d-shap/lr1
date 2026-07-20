package ru.d_shap.lr1.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.ebnf.model.EbnfRule;

/**
 * Converts EBNF grammar to LR(1) Production rules.
 *
 * @author Dmitry Shapovalov
 */
public final class GrammarConverter {

    private final EbnfGrammar _ebnfGrammar;

    private final Map<String, List<Production>> _grammarMap;

    private final List<Production> _allProductions;

    /**
     * Create a new grammar converter.
     *
     * @param ebnfGrammar the EBNF grammar to convert.
     */
    public GrammarConverter(final EbnfGrammar ebnfGrammar) {
        super();
        _ebnfGrammar = ebnfGrammar;
        _grammarMap = new HashMap<>();
        _allProductions = new ArrayList<>();
        convertGrammar();
    }

    /**
     * Convert EBNF grammar to production rules.
     */
    private void convertGrammar() {
        int ruleNumber = 0;

        for (EbnfRule ebnfRule : _ebnfGrammar.getRules()) {
            String lhs = ebnfRule.getName();

            // For now, create a simple production for each rule
            // This is a placeholder - real conversion would need to handle
            // EBNF constructs like choice, optional, repeat
            List<String> rhs = new ArrayList<>();
            rhs.add("expr");  // Placeholder RHS

            Production production = new Production(lhs, rhs, ruleNumber);
            _allProductions.add(production);

            List<Production> productions = _grammarMap.get(lhs);
            if (productions == null) {
                productions = new ArrayList<>();
                _grammarMap.put(lhs, productions);
            }
            productions.add(production);

            ruleNumber++;
        }
    }

    /**
     * Get grammar as map of non-terminal to productions.
     *
     * @return the grammar map.
     */
    public Map<String, List<Production>> getGrammarMap() {
        return new HashMap<>(_grammarMap);
    }

    /**
     * Get all productions.
     *
     * @return list of all productions.
     */
    public List<Production> getAllProductions() {
        return new ArrayList<>(_allProductions);
    }

}
