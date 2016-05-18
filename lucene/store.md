# store(存储)

这层的目的是数据交换层，目的是承上启下，对上层透明数据的存储和读取，对下层透明数据的编码和组织。分层很重要啊。

`Directory`作为文件目录的抽象类，你可以理解成为文件系统的一个目录，虽然实际上并不能这么简单认为，因为文件不只是可以放在文件系统中，也可以在内存中，在分布式文件系统，还是通过jdbc存储在数据库系统中 。里面有几个重要的方法要提下：

1. `String[] listAll()`返回索引文件目录下的所有文件
1. `IndexOutput createOutput(String name, IOContext context)`创建指定名称的新文件（这里再强调下，虽然我说了*文件*两字，但是不要直接理解成文件系统的文件）。注意返回的`IndexOutput`实例是非线程安全的，所以任何一个文件的写操作必需在一个线程内。
1. `IndexInput openInput(String name, IOContext context)`和`createOutput`相反，读取索引文件。无论是IndexOutput还是IndexIntput，操作的都是索引文件最底层的数据，比如写入读取 int，string，不负责索引的编码协议之类，如B树结构，Lucene4x还是Lucene50格式。

说到存储，就要考虑并发的问题。我们往文件系统写数据时，如果同一时刻多个进程/线程写入，会导致整个索引文件被破坏。所以要一个机制保证这个并发安全性。对索引文件的操作，IndexWriter和IndexSearch的要求不相同。对IndexWriter来说，同一时间只能有初始化一个对象来操作文件，而IndexSearch来说可以多个同时存在。这个很好理解，因为写操作要保证数据同步一致。Lucene怎么做到这一点呢？它把并发控制操作的逻辑抽象在两个类中：LockFactory 和 Lock 中。这两个类是抽象类，实现了不同场景和要求的具体子类。这里重点记下默认的文件系统锁机制实现。类`NativeFSLockFactory`中可以看出，通过文件通道`FileChannel`的`tryLock`方法来获得锁。有点要注意的是，即使当锁文件`write.lock`存在时，也可能没有被锁住，这个是交给操作系统底层完成，可以防止在jvm意外退出时，也能保障锁的一致。

## DataOutput，DataInput
负责底层数据的写入和读取，包括 byte，bytes, int, short, string, Map<string,string>, Set<string>...。

### Varint（变长整数）
这里重点要讲下 varint，我们知道 int 不论大小都是用 4 个字节表示，1 和 2147483647 都会占用相同的存储空间，显然这很浪费，优化方式就是不同大小的数字用不用长度的字节存储。在 Lucene 中也是采用这种方式，或者说 Lucene 就是采用了 [Google Protocal Buffer](https://developers.google.com/protocol-buffers/docs/encoding#simple) 的处理方式。

经过变长处理后的数据，每个字节只有低 7 位存储原始数据，最高 1 位做为标识位，如果为 1 表示后续还有数据，如果为 0 则表示达到数据末尾。

对于负整数，最高位是 1 ，这有个问题就是很浪费存储空间，比如 -1 ，也需要 5 个字节。ZigZag 算法就是应对这种问题。它会把绝对值比较小的负数变成正整数。算法公式是 (i << 1) ^ (i >> 31)。


