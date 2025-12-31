package com.github.aufdemrand.spaceportsupport

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.lexer.Lexer
import com.intellij.lexer.EmptyLexer
import com.intellij.openapi.editor.HighlighterColors

class LaunchpadSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = EmptyLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return emptyArray()
    }
}
