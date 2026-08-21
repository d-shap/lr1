/// ////////////////////////////////////////////////////////////////////////////////////////////////
// LR(1) parser implementation.
// Copyright (C) 2026 Dmitry Shapovalov.
//
// This file is part of LR(1) parser.
//
// LR(1) parser is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// LR(1) parser is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program. If not, see <http://www.gnu.org/licenses/>.
/// ////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ru.d_shap.lr1.ast.ASTNode;
import ru.d_shap.lr1.ast.ASTTreePrinter;
import ru.d_shap.lr1.ebnf.EbnfGrammar;
import ru.d_shap.lr1.parser.ActionGotoTable;
import ru.d_shap.lr1.parser.EbnfParser;
import ru.d_shap.lr1.parser.EbnfToken;
import ru.d_shap.lr1.parser.EbnfTokenizer;
import ru.d_shap.lr1.parser.FirstSetComputer;
import ru.d_shap.lr1.parser.GrammarConverter;
import ru.d_shap.lr1.parser.LR1Item;
import ru.d_shap.lr1.parser.LR1ItemSet;
import ru.d_shap.lr1.parser.LR1StateBuilder;
import ru.d_shap.lr1.parser.LR1TableBuilder;
import ru.d_shap.lr1.parser.Production;
import ru.d_shap.lr1.validator.EbnfValidator;

/**
 * The test runner.
 *
 * @author Dmitry Shapovalov
 */
public final class TestRunner {

    /**
     * Create new object.
     */
    public TestRunner() {
        super();
    }

    @Test
    public void runMath() throws Tokenizer.TokenizerException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("math.ebnf");
        List<EbnfToken> ebnfTokens = EbnfTokenizer.tokenize(inputStream);
        System.out.println("=== TOKENS ===");
        System.out.println(ebnfTokens);

        EbnfGrammar ebnfGrammar = EbnfParser.parse(ebnfTokens);
        System.out.println("\n=== EBNF GRAMMAR ===");
        System.out.println(ebnfGrammar);

        EbnfValidator ebnfValidator = new EbnfValidator(ebnfGrammar);
        ebnfValidator.validate();

        // Convert EBNF grammar to Production rules
        GrammarConverter grammarConverter = new GrammarConverter(ebnfGrammar);
        Map<String, List<Production>> grammarMap = grammarConverter.getGrammarMap();
        List<Production> allProductions = grammarConverter.getAllProductions();

        System.out.println("\n=== PRODUCTIONS ===");
        for (Production production : allProductions) {
            System.out.println(production.getRuleNumber() + ": " + production);
        }

        // Compute FIRST sets
        FirstSetComputer firstSetComputer = new FirstSetComputer(grammarMap);
        firstSetComputer.compute();
        Map<String, Set<String>> firstSets = firstSetComputer.getAllFirstSets();

        System.out.println("\n=== FIRST SETS ===");
        System.out.println(firstSets);

        // Build LR(1) states
        String startSymbol = ebnfGrammar.getRule(0).getName();
        LR1StateBuilder stateBuilder = new LR1StateBuilder(grammarMap, firstSets);

        // Create initial item: [S' → S •, $]
        Set<LR1Item> initialItems = new HashSet<>();
        Production startProduction = new Production("S'", Collections.singletonList(startSymbol), -1);
        LR1Item initialItem = new LR1Item(startProduction, 0, "$");
        initialItems.add(initialItem);

        List<LR1ItemSet> states = stateBuilder.buildStates(initialItems);

        System.out.println("\n=== LR(1) STATES ===");
        for (LR1ItemSet state : states) {
            System.out.println("State " + state.getStateNumber() + ":");
            for (LR1Item item : state.getItems()) {
                System.out.println("  " + item);
            }
        }

        // Build ACTION/GOTO tables
        LR1TableBuilder tableBuilder = new LR1TableBuilder(states, stateBuilder.getAllTransitions(), grammarMap);
        ActionGotoTable table = tableBuilder.buildTables();

        System.out.println("\n=== ACTION/GOTO TABLES ===");
        System.out.println(table.printTables(allProductions));

        // Debug: print all terminals from productions
        System.out.println("\n=== TERMINALS FROM GRAMMAR ===");
        Set<String> allTerminals = new HashSet<>();
        for (Production prod : allProductions) {
            for (String symbol : prod.getRhs()) {
                if (!grammarMap.containsKey(symbol)) {
                    allTerminals.add(symbol);
                }
            }
        }
        System.out.println(allTerminals);

        GrammarTokenizer grammarTokenizer = new GrammarTokenizer();
        grammarTokenizer.initializeFromGrammar(grammarMap, allProductions);
        List<Token> tokens = grammarTokenizer.tokenize("21+3^2/sin(11.2*0.21)");
        System.out.println("\n=== TOKENS ===");
        System.out.println(tokens);

        LRParser parser = new LRParser(table, allProductions);
        ParseResult result = parser.parse(tokens);

        if (result.isSuccess()) {
            System.out.println("\n=== AST ===");
            ASTNode ast = result.getAST();
            System.out.println(ASTTreePrinter.print(ast));
        } else {
            System.out.println("\n=== ERROR ===");
            System.out.println(result.getErrorMessage());
            System.out.println(result.getErrorLocationString());
        }
    }

}
