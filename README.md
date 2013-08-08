学习笔记
==========
1. [netty](netty/list.md)
2. spring
3. lucence


```java
  private final ByteOrder defaultOrder;

    /**
     * Creates a new factory whose default {@link ByteOrder} is
     * {@link ByteOrder#BIG_ENDIAN}.
     */
    protected AbstractChannelBufferFactory() {
        this(ByteOrder.BIG_ENDIAN);
    }
```