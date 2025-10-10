package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

class DocumentTest {

    @Test
    void shouldGenerateWithValidDocument() {
        Document document = new Document("12345678901");
        assertThat(document.value()).isEqualTo("12345678901");
    }

    @Test
    void shouldTrimDocumentWhitespace() {
        Document document = new Document("  12345678901  ");
        assertThat(document.value()).isEqualTo("12345678901");
    }

    @Test
    void shouldNotAcceptNullValue() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new Document(null));
    }

    @Test
    void shouldNotAcceptBlankDocument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document(""));
    }

    @Test
    void shouldNotAcceptEmptyDocument() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Document("   "));
    }

    @Test
    void shouldAcceptDocumentWithSpecialCharacters() {
        Document document = new Document("123.456.789-01");
        assertThat(document.value()).isEqualTo("123.456.789-01");
    }

    @Test
    void shouldAcceptDocumentWithLetters() {
        Document document = new Document("ABC123456");
        assertThat(document.value()).isEqualTo("ABC123456");
    }

    @Test
    void shouldReturnCorrectToString() {
        Document document = new Document("12345678901");
        assertThat(document.toString()).isEqualTo("12345678901");
    }
}
