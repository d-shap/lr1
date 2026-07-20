///////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////
package ru.d_shap.lr1;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import ru.d_shap.lr1.ebnf.EbnfParser;
import ru.d_shap.lr1.ebnf.EbnfToken;
import ru.d_shap.lr1.ebnf.EbnfTokenizer;
import ru.d_shap.lr1.ebnf.model.EbnfGrammar;
import ru.d_shap.lr1.state.FirstSetComputer;

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
    public void runIt() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("math.ebnf");
        List<EbnfToken> tokens = EbnfTokenizer.tokenize(inputStream);
        System.out.println(tokens);
        EbnfGrammar grammar = EbnfParser.parse(tokens);
        System.out.println(grammar);
        FirstSetComputer firstSetComputer = new FirstSetComputer(grammar);
        firstSetComputer.compute();
        Map<String, Set<String>> firstSets = firstSetComputer.getAllFirstSets();
        System.out.println(firstSets);
    }

}
