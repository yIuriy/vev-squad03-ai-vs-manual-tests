package org.apache.commons.lang3;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class F09Test {

    @Test
    public void testConstructorCanBeInstantiated() {
        SerializationUtils utils = new SerializationUtils();
        assertNotNull(utils);
    }

    @Test
    public void testCloneNullReturnsNull() {
        assertNull(SerializationUtils.clone(null));
    }

    @Test
    public void testClonePrimitiveWrapperReturnsEqualValue() {
        Integer original = 12345;
        Integer cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneIntegerBoundaryMinimum() {
        Integer original = Integer.MIN_VALUE;
        Integer cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneIntegerBoundaryMaximum() {
        Integer original = Integer.MAX_VALUE;
        Integer cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneEmptyStringReturnsEmptyString() {
        String original = "";
        String cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneCustomObjectReturnsEqualObject() {
        Person original = new Person("Alice", 30);
        Person cloned = SerializationUtils.clone(original);
        assertEquals(original, cloned);
    }

    @Test
    public void testCloneCustomObjectReturnsDistinctInstance() {
        Person original = new Person("Bob", 25);
        Person cloned = SerializationUtils.clone(original);
        assertNotSame(original, cloned);
    }

    @Test
    public void testCloneCreatesDeepCopy() {
        Container original = new Container();
        original.getItems().add("first");
        Container cloned = SerializationUtils.clone(original);
        cloned.getItems().add("second");

        assertEquals(1, original.getItems().size());
        assertEquals(2, cloned.getItems().size());
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithUnserializableFieldThrowsSerializationException() {
        SerializationUtils.clone(new NonSerializableFieldHolder());
    }

    @Test(expected = SerializationException.class)
    public void testCloneWithFailingReadObjectThrowsSerializationException() {
        SerializationUtils.clone(new ReadObjectExceptionHolder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeToStreamNullStreamThrowsIllegalArgumentException() {
        SerializationUtils.serialize("test", null);
    }

    @Test
    public void testSerializeToStreamNullObjectWritesSuccessfully() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize(null, baos);
        byte[] bytes = baos.toByteArray();
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testSerializeToStreamValidObjectWritesSuccessfully() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SerializationUtils.serialize("hello", baos);
        assertTrue(baos.toByteArray().length > 0);
    }

    @Test
    public void testSerializeToStreamClosesStream() {
        TrackCloseOutputStream out = new TrackCloseOutputStream();
        SerializationUtils.serialize("content", out);
        assertTrue(out.isClosed());
    }

    @Test(expected = SerializationException.class)
    public void testSerializeToStreamIOExceptionThrowsSerializationException() {
        FailingOutputStream out = new FailingOutputStream();
        SerializationUtils.serialize("fail", out);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeToStreamUnserializableObjectThrowsSerializationException() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SerializationUtils.serialize(new NonSerializableFieldHolder(), out);
    }

    @Test
    public void testSerializeByteArrayNullObjectReturnsNonNullByteArray() {
        byte[] bytes = SerializationUtils.serialize(null);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testSerializeByteArrayValidObjectReturnsNonNullByteArray() {
        byte[] bytes = SerializationUtils.serialize("sample");
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeByteArrayUnserializableObjectThrowsSerializationException() {
        SerializationUtils.serialize(new NonSerializableFieldHolder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeStreamNullStreamThrowsIllegalArgumentException() {
        SerializationUtils.deserialize((InputStream) null);
    }

    @Test
    public void testDeserializeStreamNullObjectReturnsNull() {
        byte[] bytes = SerializationUtils.serialize(null);
        Object result = SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
        assertNull(result);
    }

    @Test
    public void testDeserializeStreamValidObjectReturnsEqualObject() {
        byte[] bytes = SerializationUtils.serialize("data");
        Object result = SerializationUtils.deserialize(new ByteArrayInputStream(bytes));
        assertEquals("data", result);
    }

    @Test
    public void testDeserializeStreamClosesStream() {
        byte[] bytes = SerializationUtils.serialize("data");
        TrackCloseInputStream in = new TrackCloseInputStream(bytes);
        SerializationUtils.deserialize(in);
        assertTrue(in.isClosed());
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeStreamInvalidDataThrowsSerializationException() {
        byte[] invalid = new byte[] { 1, 2, 3, 4, 5 };
        SerializationUtils.deserialize(new ByteArrayInputStream(invalid));
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeStreamIOExceptionThrowsSerializationException() {
        FailingInputStream in = new FailingInputStream();
        SerializationUtils.deserialize(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeByteArrayNullDataThrowsIllegalArgumentException() {
        SerializationUtils.deserialize((byte[]) null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeByteArrayEmptyDataThrowsSerializationException() {
        SerializationUtils.deserialize(new byte[0]);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeByteArrayInvalidDataThrowsSerializationException() {
        SerializationUtils.deserialize(new byte[] { 0, 1, 2, 3 });
    }

    @Test
    public void testDeserializeByteArrayNullObjectReturnsNull() {
        byte[] bytes = SerializationUtils.serialize(null);
        Object result = SerializationUtils.deserialize(bytes);
        assertNull(result);
    }

    @Test
    public void testDeserializeByteArrayValidObjectReturnsEqualObject() {
        String original = "roundtrip";
        byte[] bytes = SerializationUtils.serialize(original);
        Object result = SerializationUtils.deserialize(bytes);
        assertEquals(original, result);
    }

    private static class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String name;
        private final int age;

        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age && (name != null ? name.equals(person.name) : person.name == null);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            return 31 * result + age;
        }
    }

    private static class Container implements Serializable {
        private static final long serialVersionUID = 1L;
        private final List<String> items = new ArrayList<>();

        public List<String> getItems() {
            return items;
        }
    }

    private static class NonSerializableFieldHolder implements Serializable {
        private static final long serialVersionUID = 1L;
        @SuppressWarnings("unused")
        private final Object nonSerializable = new Object();
    }

    private static class ReadObjectExceptionHolder implements Serializable {
        private static final long serialVersionUID = 1L;

        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            throw new IOException("Simulated read failure");
        }
    }

    private static class TrackCloseOutputStream extends ByteArrayOutputStream {
        private boolean closed = false;

        @Override
        public void close() throws IOException {
            this.closed = true;
            super.close();
        }

        public boolean isClosed() {
            return closed;
        }
    }

    private static class TrackCloseInputStream extends ByteArrayInputStream {
        private boolean closed = false;

        public TrackCloseInputStream(byte[] buf) {
            super(buf);
        }

        @Override
        public void close() throws IOException {
            this.closed = true;
            super.close();
        }

        public boolean isClosed() {
            return closed;
        }
    }

    private static class FailingOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            throw new IOException("Simulated stream write failure");
        }
    }

    private static class FailingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("Simulated stream read failure");
        }
    }
}