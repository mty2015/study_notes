Fluent Python 读书手记

- Python数据模型：特殊方法用来给整个语言模型特殊使用，一致性体现。如：\__len\__, \__getitem\__
- AOP: zope.inteface 
- 列表推导（list comprehensive）和 表达式生成器（generator expression），分别用 [...] 和 (...)表未。后者是迭代生成，更节省内存。
- 元组tuple两大功能特性：不可变、用作数据记录结构（位置信息）。元组里的元素最好也是不可变数据。
- array 和 列表相比的区别和优势。array只能存单一数据类型，比如array[int]，而列表是任何数据类型的容器。但是array在存储上更节约内存，因为实现就是分配连续的内存记录二进制数据表示。
- dict的 setdefault，可以方便处理可能不存在键的值操作。
- dict的键是可散列的，满足三个条件。支持hash()函数，通过\__hash__\实现；通过 \__eq\__ 检测相等性；若 a == b，则 hash(a) == hash(b)
