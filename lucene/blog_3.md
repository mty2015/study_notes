# 数据准备
这里为了更简单清晰的描述算法本身，演示数据尽可能简单点。假设有三篇文档（document），每个文档只有一个字段（field），字段名叫name。

  > doc0: tony is tony, my name is feiei  
  > doc1: tony hehe  
  > doc2: welcome pony
  
查询条件（query）“name, tony”先被分词成两个词语(term)：name 和 tony，然后返回所有包含了 name 或者（or）tony 两个词语的文档。从上面的准备数据文档中，将会返回doc0和doc1两个文档。正确返回所有文档不难，但还要返回文档的优先级顺序，`顺序`在很多场景下都是很重要的因素，人们总是喜欢优先看到更匹配的内容。可以把每个返回的文档计算一个得分（score），然后按照得分大小排序后返回即可。计算得分的规则有不同的方式，下面我们就聊下几种算法。

# TF-IDF算法

TF-IDF算法的入门介绍可以参考「Information Retrieval」<sup>[1](#myfootnote1)</sup>第六章，

# BM25算法介绍

# 概率算法

# Lucene实现

# ElasticSearch增强实现



aa  
aaa  
aaa  
aa  
aa  

aa  a

aa  a

aa  
aa  
aa  
aa  a

aa  
aa  a

aa  
aa  
aa  a

aa  
aa  
aa  
aa  
aa  
aa  a
a
a
a
a
a
a
a
a
a
a
a
a






















<a name="myfootnote1">1</a>: Footnote content goes here

