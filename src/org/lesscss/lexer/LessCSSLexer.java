package org.lesscss.lexer;

import java.util.*;

import org.lesscss.jcclexer.LessCSSParserTokenManager;
import org.lesscss.jcclexer.LessCSSParserConstants;
import org.lesscss.jcclexer.SimpleCharStream;
import org.lesscss.jcclexer.Token;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * @author Brian Armstrong <brian@killermonk.com>
 */
public class LessCSSLexer implements Lexer<LessCSSTokenId>, LessCSSParserConstants, LessCSSExtraConstants {

    private LexerRestartInfo<LessCSSTokenId>    info;
    private SimpleCharStream                    stream;
    private LessCSSParserTokenManager           tokenManager;
    private List<Integer>                       tokenTypeCache;

    LessCSSLexer(LexerRestartInfo<LessCSSTokenId> info) {
        this.info = info;
        stream = new SimpleCharStream(info.input());
        tokenManager = new LessCSSParserTokenManager(stream);
        tokenTypeCache = new ArrayList<Integer>();
    }

    /**
     * Get whether a token is a selector
     * @param token_kind - the kind we are checking
     * @return - whether it was a selector type or not
     */
    protected boolean isSelector(int token_kind) {
        return (
            token_kind == SELECTOR_ATTRIBUTE
            ||
            token_kind == CLASS_SELECTOR
            ||
            token_kind == ID_SELECTOR
            ||
            token_kind == SELECTOR
        );
    }

    /**
     * Get the next token that is not whitespace, caching the whitespace tokens
     * @return
     */
    protected Token getNextNonWhitespaceToken() {
        Token next;
        while (true) {
            // The lexer doesn't act like I think it should sometimes
            int currRead = info.input().readLength();
            next = tokenManager.getNextToken();
            int newLen = info.input().readLength();

            if ((newLen - currRead) < 1) {
                return null;
            }
            if (next.kind == WHITESPACE) {
                tokenTypeCache.add(WHITESPACE);
            } else {
                break;
            }
        }

        return next;
    }

    /**
     * Do our look aheads to more accurately determine our token type
     * @param token_kind - the kind we currently found
     * @return - the kind it really should be
     */
    protected int doLookAheads(int token_kind) {
        // So we can backup properly at the end
        int startLen = info.input().readLength();

        Token next = getNextNonWhitespaceToken();
        if (next == null) {
            return token_kind;
        }

        // If we have a selector
        if (isSelector(token_kind)) {
            // If tihs is a CSS Attribute
            if (next.kind == COLON) {
                // Cache the next two token, what we want them to be
                tokenTypeCache.add(COLON);
                // Scan to the ';'
                while (true) {
                    next = getNextNonWhitespaceToken();
                    if (next == null) {
                        break;
                    }

                    if (isSelector(next.kind)) {
                        tokenTypeCache.add(CSS_VALUE);
                    } else {
                        tokenTypeCache.add(next.kind);
                        if (next.kind == SEMICOLON) {
                            break;
                        }
                    }
                }

                if (token_kind != VARIABLE_SELECTOR) {
                    token_kind = CSS_ATTRIBUTE;
                }
            }
            // If this could be a mixin
            else if ((token_kind == VARIABLE_SELECTOR || token_kind == CLASS_SELECTOR) && next.kind == LPAREN) {
                token_kind = MIXIN_NAME;
            }
        }

        // Rewind past the lookahead
        int endLen = info.input().readLength();
        info.input().backup(endLen - startLen);

        return token_kind;
    }

    @Override
    public org.netbeans.api.lexer.Token<LessCSSTokenId> nextToken() {
        Token token = tokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        
        int token_kind = token.kind;
        if (tokenTypeCache.isEmpty()) {
            if (token_kind != WHITESPACE) {
                token_kind = doLookAheads(token_kind);
            }
        } else {
            token_kind = tokenTypeCache.remove(0);
        }
        return info.tokenFactory().createToken(LessCSSLanguageHierarchy.getToken(token_kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
