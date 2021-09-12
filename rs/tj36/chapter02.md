# 基于内容的推荐

## 内容推荐本质是一个信息检索系统

这一点我深有体会，第一次接触推荐系统并用于线上环境时，其实从来没有接受过推荐系统的理论体系知识，但是对 `Lucene` 和 `ES` 是比较熟悉的，所以完全按照自己的理解并使用信息检索系统做为技术栈完成的。流程就是：文本结构化处理和抽取标签，做物品画像；大数据统计用户行为建立用户画像；计算用户和物品的相似度，找出最相关的物品并且用户未消费的。


##  一些算法

1. 相似度计算一般使用余弦向量相邻度计算公司，或者 BM25F，这些代码可以直接参考 Lucene。
