package org.lesscss.lexer;

import java.util.*;
import org.lesscss.jcclexer.LessCSSParserConstants;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * @author Brian Armstrong <brian@killermonk.com>
 */
public class LessCSSLanguageHierarchy extends LanguageHierarchy<LessCSSTokenId> implements LessCSSParserConstants, LessCSSExtraConstants {

    private static List<LessCSSTokenId>         tokens;
    private static Map<Integer,LessCSSTokenId>  idToToken;

    private static void init() {
        tokens = Arrays.<LessCSSTokenId>asList(new LessCSSTokenId[] {
            new LessCSSTokenId("EOF", "whitespace", EOF),
            new LessCSSTokenId("WHITESPACE", "whitespace", WHITESPACE),
            new LessCSSTokenId("MULTI_LINE_COMMENT", "comment", MULTI_LINE_COMMENT),
            new LessCSSTokenId("SINGLE_LINE_COMMENT", "comment", SINGLE_LINE_COMMENT),
            new LessCSSTokenId("IMPORT", "keyword", IMPORT),
            new LessCSSTokenId("IMPORTANT", "reserved", IMPORTANT),
            new LessCSSTokenId("URL", "keyword", URL),
            new LessCSSTokenId("URI", "keyword", URI),
            new LessCSSTokenId("LPAREN", "operator", LPAREN),
            new LessCSSTokenId("RPAREN", "operator", RPAREN),
            new LessCSSTokenId("LBRACE", "operator", LBRACE),
            new LessCSSTokenId("RBRACE", "operator", RBRACE),
            new LessCSSTokenId("LBRACKET", "operator", LBRACKET),
            new LessCSSTokenId("RBRACKET", "operator", RBRACKET),
            new LessCSSTokenId("COLON", "operator", COLON),
            new LessCSSTokenId("SEMICOLON", "operator", SEMICOLON),
            new LessCSSTokenId("COMMA", "operator", COMMA),
            new LessCSSTokenId("AT", "operator", AT),
            new LessCSSTokenId("DOT", "operator", DOT),
            new LessCSSTokenId("HASH", "operator", HASH),
            new LessCSSTokenId("ASSIGN", "operator", ASSIGN),
            new LessCSSTokenId("LT", "operator", LT),
            new LessCSSTokenId("GT", "operator", GT),
            new LessCSSTokenId("PLUS", "operator", PLUS),
            new LessCSSTokenId("MINUS", "operator", MINUS),
            new LessCSSTokenId("STAR", "operator", STAR),
            new LessCSSTokenId("SLASH", "operator", SLASH),
            new LessCSSTokenId("MEASUREMENT", "literal", MEASUREMENT),
            new LessCSSTokenId("EM", "keyword", EM),
            new LessCSSTokenId("UNIT", "keyword", UNIT),
            new LessCSSTokenId("PIXELS", "keyword", PIXELS),
            new LessCSSTokenId("PERCENT", "keyword", PERCENT),
            new LessCSSTokenId("INCHES", "keyword", INCHES),
            new LessCSSTokenId("CENTIMETERS", "keyword", CENTIMETERS),
            new LessCSSTokenId("MILIMETERS", "keyword", MILIMETERS),
            new LessCSSTokenId("EX", "keyword", EX),
            new LessCSSTokenId("POINT", "keyword", POINT),
            new LessCSSTokenId("PICA", "keyword", PICA),
            new LessCSSTokenId("MS", "keyword", MS),
            new LessCSSTokenId("S", "keyword", S),
            new LessCSSTokenId("DEGREE", "keyword", DEGREE),
            new LessCSSTokenId("NUMBER_LITERAL", "literal", NUMBER_LITERAL),
            new LessCSSTokenId("INTEGER_LITERAL", "literal", INTEGER_LITERAL),
            new LessCSSTokenId("DECIMAL_LITERAL", "literal", DECIMAL_LITERAL),
            new LessCSSTokenId("HEX_LITERAL", "literal", HEX_LITERAL),
            new LessCSSTokenId("STRING_LITERAL", "string", STRING_LITERAL),
            new LessCSSTokenId("CHARACTER_LITERAL", "string", CHARACTER_LITERAL),
            new LessCSSTokenId("QUOTE", "string", QUOTE),
            new LessCSSTokenId("URL_VALUE", "url", URL_VALUE),
            new LessCSSTokenId("URI_VALUE", "url", URI_VALUE),
            new LessCSSTokenId("SELECTOR_ATTRIBUTE", "selector", SELECTOR_ATTRIBUTE),
            new LessCSSTokenId("VARIABLE_SELECTOR", "variable", VARIABLE_SELECTOR),
            new LessCSSTokenId("CLASS_SELECTOR", "selector", CLASS_SELECTOR),
            new LessCSSTokenId("PSEDUO_SELECTOR", "selector", PSEUDO_SELECTOR),
            new LessCSSTokenId("ID_SELECTOR", "selector", ID_SELECTOR),
            new LessCSSTokenId("SELECTOR", "selector", SELECTOR),
            new LessCSSTokenId("START_CHARS", "literal", START_CHARS),
            new LessCSSTokenId("VALID_CHARS", "literal", VALID_CHARS),
            new LessCSSTokenId("CSS_ATTRIBUTE", "css_attribute", CSS_ATTRIBUTE),
            new LessCSSTokenId("CSS_VALUE", "css_value", CSS_VALUE),
            new LessCSSTokenId("MIXIN_NAME", "mixin", MIXIN_NAME),
            new LessCSSTokenId("WILDCARD", "operator", WILDCARD), // Catchall, keep at bottom
        });
        idToToken = new HashMap<Integer, LessCSSTokenId>();
        for (LessCSSTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized LessCSSTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    protected synchronized Collection<LessCSSTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    protected synchronized Lexer<LessCSSTokenId> createLexer(LexerRestartInfo<LessCSSTokenId> info) {
        return new LessCSSLexer(info);
    }

    protected String mimeType() {
        return "text/x-lesscss";
    }
}
