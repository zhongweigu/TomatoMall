<script setup>
import testPdf from '../assets/pdf/25.pdf';
import {useRoute} from "vue-router";
import {onMounted, ref} from "vue";
const route = useRoute();
const bookId = route.params.id;

const pdfUrl = ref(''); // 存储 PDF URL

const getBookContent = async () => {
  try {
    // 根据 bookId 获取对应的文件名
    const fileName = bookId + '.pdf';

    if (!fileName) {
      throw new Error('未找到该书籍的 PDF 文件');
    }

    // 动态导入 PDF 文件
    const pdfModule = await import(/* @vite-ignore */ `../assets/pdf/${fileName}`);
    pdfUrl.value = pdfModule.default;
  } catch (error) {
    console.error('加载 PDF 失败:', error);
    // 可以在这里设置回退 URL 或显示错误信息
  }
};

onMounted(getBookContent);

</script>

<template>
  <div class="pdf-container">
    <iframe :src="pdfUrl" class="pdf-viewer"></iframe>
  </div>
</template>

<style scoped>
.pdf-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.pdf-viewer {
  width: 100%;
  height: 100%;
  border: none; /* 去除iframe的边框 */
}
</style>