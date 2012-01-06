package org.lesscss.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 * @author Brian Armstrong <brian@killermonk.com>
 */
public class LessCSSTokenId implements TokenId {

    private final String    name;
    private final String    primaryCategory;
    private final int       id;

    private static final Language<LessCSSTokenId> language = new LessCSSLanguageHierarchy().language();

    public static final Language<LessCSSTokenId> getLanguage() {
        return language;
    }

    LessCSSTokenId(String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory() {
        return primaryCategory;
    }

    @Override
    public int ordinal() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }
}
