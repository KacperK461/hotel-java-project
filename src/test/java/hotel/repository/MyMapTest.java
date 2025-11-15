package hotel.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class MyMapTest {

    private MyMap<Integer, String> map;

    @BeforeEach
    void setUp() {
        map = new MyMap<>();
    }

    @Test
    void testPutAndGet() {
        map.put(1, "value1");
        assertEquals("value1", map.get(1));
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(map.get(999));
    }

    @Test
    void testPutUpdateExistingKey() {
        map.put(1, "original");
        map.put(1, "updated");
        assertEquals("updated", map.get(1));
    }

    @Test
    void testPutNullKey() {
        map.put(null, "nullKey");
        assertEquals("nullKey", map.get(null));
    }

    @Test
    void testPutNullValue() {
        map.put(1, null);
        assertNull(map.get(1));
    }

    @Test
    void testKeysEmptyMap() {
        List<Integer> keys = map.keys();
        assertTrue(keys.isEmpty());
    }

    @Test
    void testKeysSingleItem() {
        map.put(1, "value1");
        List<Integer> keys = map.keys();
        assertEquals(1, keys.size());
        assertTrue(keys.contains(1));
    }

    @Test
    void testKeysMultipleItems() {
        map.put(1, "value1");
        map.put(2, "value2");
        map.put(3, "value3");

        List<Integer> keys = map.keys();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertTrue(keys.contains(3));
    }

    @Test
    void testKeysReturnsCopy() {
        map.put(1, "value1");
        List<Integer> keys1 = map.keys();
        List<Integer> keys2 = map.keys();

        assertNotSame(keys1, keys2);
        assertEquals(keys1, keys2);
    }

    @Test
    void testKeysOrderPreserved() {
        map.put(3, "third");
        map.put(1, "first");
        map.put(2, "second");

        List<Integer> keys = map.keys();
        assertEquals(Integer.valueOf(3), keys.get(0));
        assertEquals(Integer.valueOf(1), keys.get(1));
        assertEquals(Integer.valueOf(2), keys.get(2));
    }

    @Test
    void testRemoveExistingKey() {
        map.put(1, "value1");
        map.put(2, "value2");

        map.remove(1);

        assertNull(map.get(1));
        assertEquals("value2", map.get(2));
        assertEquals(1, map.keys().size());
    }

    @Test
    void testRemoveNonExistentKey() {
        map.put(1, "value1");
        map.remove(999);

        assertEquals("value1", map.get(1));
        assertEquals(1, map.keys().size());
    }

    @Test
    void testRemoveAllItems() {
        map.put(1, "value1");
        map.put(2, "value2");

        map.remove(1);
        map.remove(2);

        assertTrue(map.keys().isEmpty());
        assertNull(map.get(1));
        assertNull(map.get(2));
    }

    @Test
    void testComplexWorkflow() {
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "third");

        map.put(2, "updated_second");
        map.remove(3);

        assertEquals("first", map.get(1));
        assertEquals("updated_second", map.get(2));
        assertNull(map.get(3));
        assertEquals(2, map.keys().size());
    }

    @Test
    void testWithDifferentTypes() {
        MyMap<String, Integer> stringIntMap = new MyMap<>();

        stringIntMap.put("key1", 100);
        stringIntMap.put("key2", 200);

        assertEquals(Integer.valueOf(100), stringIntMap.get("key1"));
        assertEquals(Integer.valueOf(200), stringIntMap.get("key2"));
        assertNull(stringIntMap.get("nonexistent"));
    }

    @Test
    void testUpdatePreservesOrder() {
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "third");
        map.put(2, "updated_second");

        List<Integer> keys = map.keys();
        assertEquals(Integer.valueOf(1), keys.get(0));
        assertEquals(Integer.valueOf(2), keys.get(1));
        assertEquals(Integer.valueOf(3), keys.get(2));
    }

    @Test
    void testRemoveMiddleItemPreservesOrder() {
        map.put(1, "first");
        map.put(2, "second");
        map.put(3, "third");

        map.remove(2);

        List<Integer> keys = map.keys();
        assertEquals(2, keys.size());
        assertEquals(Integer.valueOf(1), keys.get(0));
        assertEquals(Integer.valueOf(3), keys.get(1));
    }
}

