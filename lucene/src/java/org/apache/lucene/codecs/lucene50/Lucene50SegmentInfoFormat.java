/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.codecs.lucene50;


import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.codecs.CodecUtil;
import org.apache.lucene.codecs.SegmentInfoFormat;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.IndexWriter; // javadocs
import org.apache.lucene.index.SegmentInfo; // javadocs
import org.apache.lucene.index.SegmentInfos; // javadocs
import org.apache.lucene.store.ChecksumIndexInput;
import org.apache.lucene.store.DataOutput; // javadocs
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.Version;

/**
 * Lucene 5.0 Segment info format.
 * <p>
 * Files:
 * <ul>
 *   <li><tt>.si</tt>: Header, SegVersion, SegSize, IsCompoundFile, Diagnostics, Files, Attributes, Footer
 * </ul>
 * Data types:
 * <ul>
 *   <li>Header --&gt; {@link CodecUtil#writeIndexHeader IndexHeader}</li>
 *   <li>SegSize --&gt; {@link DataOutput#writeInt Int32}</li>
 *   <li>SegVersion --&gt; {@link DataOutput#writeString String}</li>
 *   <li>Files --&gt; {@link DataOutput#writeSetOfStrings Set&lt;String&gt;}</li>
 *   <li>Diagnostics,Attributes --&gt; {@link DataOutput#writeMapOfStrings Map&lt;String,String&gt;}</li>
 *   <li>IsCompoundFile --&gt; {@link DataOutput#writeByte Int8}</li>
 *   <li>Footer --&gt; {@link CodecUtil#writeFooter CodecFooter}</li>
 * </ul>
 * Field Descriptions:
 * <ul>
 *   <li>SegVersion is the code version that created the segment.</li>
 *   <li>SegSize is the number of documents contained in the segment index.</li>
 *   <li>IsCompoundFile records whether the segment is written as a compound file or
 *       not. If this is -1, the segment is not a compound file. If it is 1, the segment
 *       is a compound file.</li>
 *   <li>The Diagnostics Map is privately written by {@link IndexWriter}, as a debugging aid,
 *       for each segment it creates. It includes metadata like the current Lucene
 *       version, OS, Java version, why the segment was created (merge, flush,
 *       addIndexes), etc.</li>
 *   <li>Files is a list of files referred to by this segment.</li>
 * </ul>
 * 
 * @see SegmentInfos
 * @lucene.experimental
 */
public class Lucene50SegmentInfoFormat extends SegmentInfoFormat {

  /** Sole constructor. */
  public Lucene50SegmentInfoFormat() {
  }
  
0000000: 3fd7 6c17 134c 7563 656e 6535 3053 6567  ?.l..Lucene50Seg
0000010: 6d65 6e74 496e 666f 0000 0001 1c3f a7a1  mentInfo.....?..
0000020: 1113 fa96 6270 42c4 3ba4 39ca 0000 0000  ....bpB.;.9.....
0000030: 0600 0000 0100 0000 0000 0000 01ff 0a02  ................
0000040: 6f73 084d 6163 204f 5320 580b 6a61 7661  os.Mac OS X.java
                       6 0000050: 2e76 656e 646f 7212 4f72 6163 6c65 2043  .vendor.Oracle C
                         7 0000060: 6f72 706f 7261 7469 6f6e 0c6a 6176 612e  orporation.java.
                           8 0000070: 7665 7273 696f 6e08 312e 382e 305f 3931  version.1.8.0_91
                             9 0000080: 0f6a 6176 612e 766d 2e76 6572 7369 6f6e  .java.vm.version
                              10 0000090: 0932 352e 3931 2d62 3134 0e6c 7563 656e  .25.91-b14.lucen
                               11 00000a0: 652e 7665 7273 696f 6e05 362e 312e 3007  e.version.6.1.0.
                                12 00000b0: 6f73 2e61 7263 6806 7838 365f 3634 146a  os.arch.x86_64.j
                                 13 00000c0: 6176 612e 7275 6e74 696d 652e 7665 7273  ava.runtime.vers
                                  14 00000d0: 696f 6e0c 312e 382e 305f 3931 2d62 3134  ion.1.8.0_91-b14
                                   15 00000e0: 0673 6f75 7263 6505 666c 7573 680a 6f73  .source.flush.os
                                    16 00000f0: 2e76 6572 7369 6f6e 0731 302e 3131 2e34  .version.10.11.4
                                     17 0000100: 0974 696d 6573 7461 6d70 0d31 3437 3531  .timestamp.14751
                                      18 0000110: 3038 3136 3232 3932 0c06 5f30 2e64 6969  08162292.._0.dii
                                       19 0000120: 055f 302e 7369 115f 305f 4c75 6365 6e65  ._0.si._0_Lucene
                                        20 0000130: 3530 5f30 2e64 6f63 115f 305f 4c75 6365  50_0.doc._0_Luce
                                         21 0000140: 6e65 3530 5f30 2e74 696d 115f 305f 4c75  ne50_0.tim._0_Lu
                                          22 0000150: 6365 6e65 3530 5f30 2e70 6f73 065f 302e  cene50_0.pos._0.
                                           23 0000160: 6e76 6406 5f30 2e66 6478 065f 302e 6469  nvd._0.fdx._0.di
                                            24 0000170: 6d11 5f30 5f4c 7563 656e 6535 305f 302e  m._0_Lucene50_0.
                                             25 0000180: 7469 7006 5f30 2e66 6474 065f 302e 6e76  tip._0.fdt._0.nv
                                              26 0000190: 6d06 5f30 2e66 6e6d 011f 4c75 6365 6e65  m._0.fnm..Lucene
                                               27 00001a0: 3530 5374 6f72 6564 4669 656c 6473 466f  50StoredFieldsFo
                                                28 00001b0: 726d 6174 2e6d 6f64 650a 4245 5354 5f53  rmat.mode.BEST_S
                                                 29 00001c0: 5045 4544 c028 93e8 0000 0000 0000 0000  PEED.(..........
                                                          30 00001d0: a88c 14ce 0a                             .....
  @Override
  public SegmentInfo read(Directory dir, String segment, byte[] segmentID, IOContext context) throws IOException {
    final String fileName = IndexFileNames.segmentFileName(segment, "", Lucene50SegmentInfoFormat.SI_EXTENSION);
    try (ChecksumIndexInput input = dir.openChecksumInput(fileName, context)) {
      Throwable priorE = null;
      SegmentInfo si = null;
      try {
        int format = CodecUtil.checkIndexHeader(input, Lucene50SegmentInfoFormat.CODEC_NAME,
                                          Lucene50SegmentInfoFormat.VERSION_START,
                                          Lucene50SegmentInfoFormat.VERSION_CURRENT,
                                          segmentID, "");
        final Version version = Version.fromBits(input.readInt(), input.readInt(), input.readInt());
        
        final int docCount = input.readInt();
        if (docCount < 0) {
          throw new CorruptIndexException("invalid docCount: " + docCount, input);
        }
        final boolean isCompoundFile = input.readByte() == SegmentInfo.YES;
        
        final Map<String,String> diagnostics;
        final Set<String> files;
        final Map<String,String> attributes;
        
        if (format >= VERSION_SAFE_MAPS) {
          diagnostics = input.readMapOfStrings();
          files = input.readSetOfStrings();
          attributes = input.readMapOfStrings();
        } else {
          diagnostics = Collections.unmodifiableMap(input.readStringStringMap());
          files = Collections.unmodifiableSet(input.readStringSet());
          attributes = Collections.unmodifiableMap(input.readStringStringMap());
        }
        
        si = new SegmentInfo(dir, version, segment, docCount, isCompoundFile, null, diagnostics, segmentID, attributes);
        si.setFiles(files);
      } catch (Throwable exception) {
        priorE = exception;
      } finally {
        CodecUtil.checkFooter(input, priorE);
      }
      return si;
    }
  }

  @Override
  public void write(Directory dir, SegmentInfo si, IOContext ioContext) throws IOException {
    final String fileName = IndexFileNames.segmentFileName(si.name, "", Lucene50SegmentInfoFormat.SI_EXTENSION);

    try (IndexOutput output = dir.createOutput(fileName, ioContext)) {
      // Only add the file once we've successfully created it, else IFD assert can trip:
      si.addFile(fileName);
      CodecUtil.writeIndexHeader(output, 
                                   Lucene50SegmentInfoFormat.CODEC_NAME, 
                                   Lucene50SegmentInfoFormat.VERSION_CURRENT,
                                   si.getId(),
                                   "");
      Version version = si.getVersion();
      if (version.major < 5) {
        throw new IllegalArgumentException("invalid major version: should be >= 5 but got: " + version.major + " segment=" + si);
      }
      // Write the Lucene version that created this segment, since 3.1
      output.writeInt(version.major);
      output.writeInt(version.minor);
      output.writeInt(version.bugfix);
      assert version.prerelease == 0;
      output.writeInt(si.maxDoc());

      output.writeByte((byte) (si.getUseCompoundFile() ? SegmentInfo.YES : SegmentInfo.NO));
      output.writeMapOfStrings(si.getDiagnostics());
      Set<String> files = si.files();
      for (String file : files) {
        if (!IndexFileNames.parseSegmentName(file).equals(si.name)) {
          throw new IllegalArgumentException("invalid files: expected segment=" + si.name + ", got=" + files);
        }
      }
      output.writeSetOfStrings(files);
      output.writeMapOfStrings(si.getAttributes());
      CodecUtil.writeFooter(output);
    }
  }

  /** File extension used to store {@link SegmentInfo}. */
  public final static String SI_EXTENSION = "si";
  static final String CODEC_NAME = "Lucene50SegmentInfo";
  static final int VERSION_START = 0;
  static final int VERSION_SAFE_MAPS = 1;
  static final int VERSION_CURRENT = VERSION_SAFE_MAPS;
}
