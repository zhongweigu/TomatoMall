// src/mock/productData.js
// src/mock/productData.js
export const mockProducts = [
  {
    id: "101",
    title: "深入理解Java虚拟机",
    price: 99.50,
    rate: 9.5,
    description: "Java开发者必读经典，全面讲解JVM工作原理",
    cover: "../photo/back.png",
    detail: "本书详细讲解了Java虚拟机的体系结构、内存管理、字节码执行等核心内容",
    specifications: [
      { id: "1001", item: "作者", value: "周志明", productId: "101" },
      { id: "1002", item: "副标题", value: "JVM高级特性与最佳实践", productId: "101" },
      { id: "1003", item: "ISBN", value: "9787111641247", productId: "101" },
      { id: "1004", item: "装帧", value: "平装", productId: "101" },
      { id: "1005", item: "页数", value: "540", productId: "101" }
    ]
  },
  {
    id: "102",
    title: "代码整洁之道",
    price: 59.00,
    rate: 9.2,
    description: "软件工程领域的经典著作",
    cover: "../photo/background.png",
    detail: "本书提出一种观念：代码质量与其整洁度成正比",
    specifications: [
      { id: "1008", item: "作者", value: "Robert C. Martin", productId: "102" },
      { id: "1009", item: "副标题", value: "程序员的职业素养", productId: "102" },
      { id: "1010", item: "ISBN", value: "9787121316633", productId: "102" }
    ]
  }
];

export const mockStockData = {
  "101": { id: "1001", productId: "101", amount: 85, frozen: 15 },
  "102": { id: "1002", productId: "102", amount: 42, frozen: 8 }
};