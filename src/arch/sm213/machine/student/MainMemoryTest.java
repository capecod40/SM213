package arch.sm213.machine.student;

import machine.AbstractMainMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainMemoryTest {
    private MainMemory memory;

    @BeforeEach
    void setup() {
        memory = new MainMemory(64);
    }

    @Test
    void memoryAligned() {
        int address = 1;
        assertTrue(memory.isAccessAligned(address, 1));
        assertFalse(memory.isAccessAligned(address, 2));

        address = 5;
        assertTrue(memory.isAccessAligned(address, 1));
        assertFalse(memory.isAccessAligned(address, 2));

        address = 6;
        assertTrue(memory.isAccessAligned(address, 1));
        assertTrue(memory.isAccessAligned(address, 2));
        assertFalse(memory.isAccessAligned(address, 4));

        address = 16;
        assertTrue(memory.isAccessAligned(address, 1));
        assertTrue(memory.isAccessAligned(address, 2));
        assertTrue(memory.isAccessAligned(address, 4));
        assertTrue(memory.isAccessAligned(address, 8));
        assertTrue(memory.isAccessAligned(address, 16));

        address = 24;
        assertTrue(memory.isAccessAligned(address, 1));
        assertTrue(memory.isAccessAligned(address, 2));
        assertTrue(memory.isAccessAligned(address, 4));
        assertTrue(memory.isAccessAligned(address, 8));
        assertFalse(memory.isAccessAligned(address, 16));
    }

    @Test
    void bytesToIntegerTest() {
        byte zero = 0;
        byte one = 0;
        byte two = 0;
        byte three = 0;
        assertEquals(0, memory.bytesToInteger(zero, one, two, three));

        zero = 1;
        one = 2;
        two = 3;
        three = 4;
        assertEquals(16909060, memory.bytesToInteger(zero, one, two, three));

        zero = (byte) 0xff;
        one = 3;
        two = (byte) 0xff;
        three = (byte) 0xae;
        assertEquals(-16515154, memory.bytesToInteger(zero, one, two, three));
    }

    @Test
    void integerToByteTest() {
        int i = 0;
        byte result[] = memory.integerToBytes(i);
        for (byte element : result) {
            assertEquals(0, element);
        }

        i = 0xffffffff;
        result = memory.integerToBytes(i);
        for (byte element : result) {
            assertEquals((byte)0xff, element);
        }

        i = 0xff03ffae;
        result = memory.integerToBytes(i);
        assertEquals((byte)0xff, result[0]);
        assertEquals((byte)0x3, result[1]);
        assertEquals((byte)0xff, result[2]);
        assertEquals((byte)0xae, result[3]);

        i = 0x030303ff;
        result = memory.integerToBytes(i);
        assertEquals((byte)0x3, result[0]);
        assertEquals((byte)0x3, result[1]);
        assertEquals((byte)0x3, result[2]);
        assertEquals((byte)0xff, result[3]);

    }

    @Test
    void setGetMemoryTest() {
        byte data[] = {12, 34, 56, 78};
        try {
            memory.set(4, data);
        } catch (Exception e) {
            fail();
        }

        byte result[] = null;
        try {
            result = memory.get(4, 4);
        } catch (Exception e) {
            fail();
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(data[i], result[i]);
        }


        byte data2[] = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff};
        try {
            memory.set(4, data2);
        } catch (Exception e) {
            fail();
        }

        try {
            result = memory.get(4, 4);
        } catch (Exception e) {
            fail();
        }

        for (int i = 0; i < 4; i++) {
            assertEquals(data2[i], result[i]);
        }

        byte data3[] = new byte[64];
        try {
            memory.set(0, data3);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void setGetMemoryExceptTest() {
        byte data[] = new byte[512];
        try {
            memory.set(32, data);
            fail();
        } catch (AbstractMainMemory.InvalidAddressException e) {
            // success
        } catch (Exception e) {
            fail();
        }

        data = new byte[0];
        try {
            memory.set(65, data);
            fail();
        } catch (AbstractMainMemory.InvalidAddressException e) {
            // success
        } catch (Exception e) {
            fail();
        }

        try {
            memory.set(-1, data);
            fail();
        } catch (AbstractMainMemory.InvalidAddressException e) {
            // success
        } catch (Exception e) {
            fail();
        }

    }

}
