Fluent Python 读书手记

- Python数据模型：特殊方法用来给整个语言模型特殊使用，一致性体现。如：\_\_len\_\_, \_\_getitem\_\_
- AOP: zope.inteface 
- 列表推导（list comprehensive）和 表达式生成器（generator expression），分别用 [...] 和 (...)表未。后者是迭代生成，更节省内存。
- 元组tuple两大功能特性：不可变、用作数据记录结构（位置信息）。元组里的元素最好也是不可变数据。
- array 和 列表相比的区别和优势。array只能存单一数据类型，比如array[int]，而列表是任何数据类型的容器。但是array在存储上更节约内存，因为实现就是分配连续的内存记录二进制数据表示。
- dict的 setdefault，可以方便处理可能不存在键的值操作。
- dict的键是可散列的，满足三个条件。支持hash()函数，通过\_\_hash\_\_实现；通过 \_\_eq\_\_ 检测相等性；若 a == b，则 hash(a) == hash(b)
- 接收函数参数或者返回函数的函数可以称为高阶函数
- 自定义类中重写 \_\_call\_\_( ) 方法，可以把类实例变为可直接调用的函数对象。
- python中的inspect模块，提供了很多内省方法，像java的reflect包。six库。
- operator 和 functools 模块中有很多定义好的高阶函数可以，方便编写函数式代码风格。
- 抽象类可以继承abc.ABC，使用 @abstractmethod 修饰。
- 因为在Python中函数也是对象，可以用来传参和返回，所以23经典设计模式在Python中可能不完全适用，或者以更简单的方式使用。
- 装饰器函数是导入模块时（load module）时就执行，而不是运行时执行。这个特性可用于框架处理一些启动注册的工作。
- 在嵌套函数中，如果要引用外部的不可变象并重新赋值，则需要使用 nolocal 关键字声明变量再操作。
- functools.lru_cache 可以缓存某个计算代价很大的函数结果。
- functools.singledispatch 可以写出类似Java中方法重载的代码
- @classmethod 和 @staticmethod 的区别。前者第一个参数是Class本身，后者就是一普通函数。
- @Property 设置getter/setter属性
- Python中接口，是指“鸭子类型”协议，即一个类具有相似的行为，但跟继承没有什么关系。但是抽象类（接口）在现实世界中是存在的，所以可以通过 abc.ABC 来定义抽象基类。
- 不要直接继承内置的类，比如 list, dict。因为继承内置类，子类重写的方法如果通过python协议调用，不会使用子类的方法。比如 A extends list, A重写了 \_\_len\_\_，但是调用 len(a) 还是使用list的内置方法。解决办法是使用 pypy中的类，比如collections.abc中的类。
- for/while/try - else 更应该按 for/while/try - then 语义理解更好些。
- GIL指CPython实现一次只允许使用一个线程执行Python字节码，因为CPython解释器本来不是安全的。但是对于IO密集型场景无害，因为标准库中所有阻塞IO都会释放GIL(global interpreter locl)。但是CPU密集型计算则会有影响。

