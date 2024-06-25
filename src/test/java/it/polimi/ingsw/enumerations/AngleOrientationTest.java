package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.enumerations.AngleOrientation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AngleOrientationTest {

    @Test
    void shouldMapEnumToXCorrectly() {
        assertEquals(-1, AngleOrientation.TOPRIGHT.mapEnumToX());
        assertEquals(-1, AngleOrientation.TOPLEFT.mapEnumToX());
        assertEquals(1, AngleOrientation.BOTTOMRIGHT.mapEnumToX());
        assertEquals(1, AngleOrientation.BOTTOMLEFT.mapEnumToX());
        assertEquals(0, AngleOrientation.NONE.mapEnumToX());
    }

    @Test
    void shouldMapEnumToYCorrectly() {
        assertEquals(1, AngleOrientation.TOPRIGHT.mapEnumToY());
        assertEquals(-1, AngleOrientation.TOPLEFT.mapEnumToY());
        assertEquals(1, AngleOrientation.BOTTOMRIGHT.mapEnumToY());
        assertEquals(-1, AngleOrientation.BOTTOMLEFT.mapEnumToY());
        assertEquals(0, AngleOrientation.NONE.mapEnumToY());
    }

    @Test
    void shouldMapEnumToXLShapedCorrectly() {
        assertEquals(1, AngleOrientation.TOPRIGHT.mapEnumToXLShaped());
        assertEquals(1, AngleOrientation.TOPLEFT.mapEnumToXLShaped());
        assertEquals(-1, AngleOrientation.BOTTOMLEFT.mapEnumToXLShaped());
        assertEquals(-1, AngleOrientation.BOTTOMRIGHT.mapEnumToXLShaped());
        assertEquals(0, AngleOrientation.NONE.mapEnumToXLShaped());
    }

    @Test
    void shouldMapEnumToYLShapedCorrectly() {
        assertEquals(1, AngleOrientation.TOPRIGHT.mapEnumToYLShaped());
        assertEquals(-1, AngleOrientation.TOPLEFT.mapEnumToYLShaped());
        assertEquals(1, AngleOrientation.BOTTOMLEFT.mapEnumToYLShaped());
        assertEquals(-1, AngleOrientation.BOTTOMRIGHT.mapEnumToYLShaped());
        assertEquals(0, AngleOrientation.NONE.mapEnumToYLShaped());
    }
}