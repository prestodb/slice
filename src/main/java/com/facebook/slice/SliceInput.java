/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.slice;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class SliceInput
        extends InputStream
        implements DataInput
{
    /**
     * Returns the {@code position} of this buffer.
     */
    public abstract long position();

    /**
     * Sets the {@code position} of this buffer.
     *
     * @throws IndexOutOfBoundsException if the specified {@code position} is
     * less than {@code 0} or
     * greater than {@code this.writerIndex}
     */
    public abstract void setPosition(long position);

    /**
     * Returns {@code true}
     * if and only if {@code available()} is greater
     * than {@code 0}.
     */
    public abstract boolean isReadable();

    /**
     * Returns the number of bytes that can be read without blocking.
     */
    @SuppressWarnings("AbstractMethodOverridesConcreteMethod")
    @Override
    public abstract int available();

    @Override
    public abstract int read();

    /**
     * Returns true if the byte at the current {@code position} is not {@code 0} and increases
     * the {@code position} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 1}
     */
    @Override
    public abstract boolean readBoolean();

    /**
     * Gets a byte at the current {@code position} and increases
     * the {@code position} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 1}
     */
    @Override
    public abstract byte readByte();

    /**
     * Gets an unsigned byte at the current {@code position} and increases
     * the {@code position} by {@code 1} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 1}
     */
    @Override
    public abstract int readUnsignedByte();

    /**
     * Gets a 16-bit short integer at the current {@code position}
     * and increases the {@code position} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 2}
     */
    @Override
    public abstract short readShort();

    /**
     * Gets an unsigned 16-bit short integer at the current {@code position}
     * and increases the {@code position} by {@code 2} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 2}
     */
    @Override
    public abstract int readUnsignedShort();

    /**
     * Gets a 32-bit integer at the current {@code position}
     * and increases the {@code position} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 4}
     */
    @Override
    public abstract int readInt();

    /**
     * Gets an unsigned 32-bit integer at the current {@code position}
     * and increases the {@code position} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 4}
     */
    public final long readUnsignedInt()
    {
        return readInt() & 0xFFFFFFFFL;
    }

    /**
     * Gets a 64-bit long at the current {@code position}
     * and increases the {@code position} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 8}
     */
    @Override
    public abstract long readLong();

    /**
     * Gets a 32-bit float at the current {@code position}
     * and increases the {@code position} by {@code 4} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 4}
     */
    @Override
    public abstract float readFloat();

    /**
     * Gets a 64-bit double at the current {@code position}
     * and increases the {@code position} by {@code 8} in this buffer.
     *
     * @throws IndexOutOfBoundsException if {@code this.available()} is less than {@code 8}
     */
    @Override
    public abstract double readDouble();

    /**
     * Returns a new slice of this buffer's sub-region starting at the current
     * {@code position} and increases the {@code position} by the size
     * of the new slice (= {@code length}).
     *
     * @param length the size of the new slice
     * @return the newly created slice
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.available()}
     */
    public abstract Slice readSlice(int length);

    @Override
    public final void readFully(byte[] destination)
    {
        readBytes(destination);
    }

    @Override
    public final int read(byte[] destination)
    {
        return read(destination, 0, destination.length);
    }

    @Override
    public abstract int read(byte[] destination, int destinationIndex, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code position} and increases the {@code position}
     * by the number of the transferred bytes (= {@code dst.length}).
     *
     * @throws IndexOutOfBoundsException if {@code dst.length} is greater than {@code this.available()}
     */
    public final void readBytes(byte[] destination)
    {
        readBytes(destination, 0, destination.length);
    }

    @Override
    public final void readFully(byte[] destination, int offset, int length)
    {
        readBytes(destination, offset, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code position} and increases the {@code position}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param destinationIndex the first index of the destination
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code destinationIndex} is less than {@code 0},
     * if {@code length} is greater than {@code this.available()}, or
     * if {@code destinationIndex + length} is greater than {@code destination.length}
     */
    public abstract void readBytes(byte[] destination, int destinationIndex, int length);

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code position} until the destination becomes
     * non-writable, and increases the {@code position} by the number of the
     * transferred bytes.  This method is basically same with
     * {@link #readBytes(Slice, int, int)}, except that this method
     * increases the {@code writerIndex} of the destination by the number of
     * the transferred bytes while {@link #readBytes(Slice, int, int)}
     * does not.
     *
     * @throws IndexOutOfBoundsException if {@code destination.writableBytes} is greater than
     * {@code this.available()}
     */
    public final void readBytes(Slice destination)
    {
        readBytes(destination, 0, destination.length());
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code position} and increases the {@code position}
     * by the number of the transferred bytes (= {@code length}).  This method
     * is basically same with {@link #readBytes(Slice, int, int)},
     * except that this method increases the {@code writerIndex} of the
     * destination by the number of the transferred bytes (= {@code length})
     * while {@link #readBytes(Slice, int, int)} does not.
     *
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.available()} or
     * if {@code length} is greater than {@code destination.writableBytes}
     */
    public final void readBytes(Slice destination, int length)
    {
        readBytes(destination, 0, length);
    }

    /**
     * Transfers this buffer's data to the specified destination starting at
     * the current {@code position} and increases the {@code position}
     * by the number of the transferred bytes (= {@code length}).
     *
     * @param destinationIndex the first index of the destination
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if the specified {@code destinationIndex} is less than {@code 0},
     * if {@code length} is greater than {@code this.available()}, or
     * if {@code destinationIndex + length} is greater than
     * {@code destination.capacity}
     */
    public abstract void readBytes(Slice destination, int destinationIndex, int length);

    /**
     * Transfers this buffer's data to the specified stream starting at the
     * current {@code position}.
     *
     * @param length the number of bytes to transfer
     * @throws IndexOutOfBoundsException if {@code length} is greater than {@code this.available()}
     * @throws java.io.IOException if the specified stream threw an exception during I/O
     */
    public abstract void readBytes(OutputStream out, int length)
            throws IOException;

    @Override
    public abstract long skip(long length);

    @Override
    public abstract int skipBytes(int length);

    @Override
    public void close()
    {
    }

    /**
     * Approximate number of bytes retained by this instance.
     */
    public abstract long getRetainedSize();

    //
    // Unsupported operations
    //

    @Override
    public final void mark(int readLimit)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final void reset()

    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean markSupported()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final char readChar()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String readLine()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String readUTF()
    {
        throw new UnsupportedOperationException();
    }
}
