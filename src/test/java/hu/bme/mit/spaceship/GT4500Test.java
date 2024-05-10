package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primary;
  private TorpedoStore secondary;

  @BeforeEach
  public void init(){
    primary = mock(TorpedoStore.class);
    secondary = mock(TorpedoStore.class);

    this.ship = new GT4500(primary, secondary);
  }

  @Test
  void fireTorpedo_Single_Success(){
    // Arrange
    when(primary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_All_Success(){
    // Arrange
    when(primary.fire(anyInt())).thenReturn(true);
    when(secondary.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_Single_FirstFire_PrimaryTS_Is_Used() {
    when(primary.fire(anyInt())).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, never()).fire(1);
  }

  @Test
  void fireTorpedo_Single_SecondFire_SecondaryTs_Is_Used() {
    ship.fireTorpedo(FiringMode.SINGLE);
    reset(primary);

    when(secondary.fire(anyInt())).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(true, result);
    verify(primary, never()).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_Single_FirstFire_ButBothEmtpy_None_Used() {
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(false, result);
    verify(primary, never()).fire(1);
    verify(secondary, never()).fire(1);
  }

  @Test
  void fireTorpedo_Single_SecondFire_ButBothEmtpy_None_Used() {
    ship.fireTorpedo(FiringMode.SINGLE);
    reset(primary);

    when(primary.isEmpty()).thenReturn(true);
    when(secondary.isEmpty()).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(false, result);
    verify(primary, never()).fire(1);
    verify(secondary, never()).fire(1);
  }

  @Test
  void fireTorpedo_Single_FirstFire_PrimaryTsEmpty_So_SecondaryTs_Is_Used() {
    when(primary.isEmpty()).thenReturn(true);
    when(secondary.fire(anyInt())).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(true, result);
    verify(primary, never()).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_Single_SecondFire_SecondaryTsEmpty_So_PrimaryTs_Is_Used() {
    ship.fireTorpedo(FiringMode.SINGLE);
    reset(primary);

    when(secondary.isEmpty()).thenReturn(true);
    when(primary.fire(anyInt())).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, never()).fire(1);
  }

  @Test
  void fireTorpedo_Single_FirstFire_PrimaryTs_Fails_Secondary_Not_Used() {
    when(primary.fire(anyInt())).thenReturn(false);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(false, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, never()).fire(1);
  }

  @Test
  void fireTorpedo_Single_SecondFire_SecondaryTsFails_Primary_Not_Used() {
    ship.fireTorpedo(FiringMode.SINGLE);
    reset(primary);

    when(secondary.fire(anyInt())).thenReturn(false);


    boolean result = ship.fireTorpedo(FiringMode.SINGLE);


    assertEquals(false, result);
    verify(primary, never()).fire(1);
    verify(secondary, times(1)).fire(1);
  }

  @Test
  void fireTorpedo_Null_FiriginMode() {
    assertThrows(NullPointerException.class, () -> {
      // Act
      ship.fireTorpedo(null);
    });
    verify(primary, never()).fire(anyInt());
    verify(secondary, never()).fire(anyInt());
  }

  @Test
  void fireTorpedo_All_BothTs_Are_Used() {
    when(primary.fire(anyInt())).thenReturn(true);
    when(secondary.fire(anyInt())).thenReturn(true);


    boolean result = ship.fireTorpedo(FiringMode.ALL);


    assertEquals(true, result);
    verify(primary, times(1)).fire(1);
    verify(secondary, times(1)).fire(1);
  }
}
