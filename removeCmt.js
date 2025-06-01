import fs from 'fs';
import path from 'path';

// Hàm loại bỏ comment trong mã nguồn ReactJS
function removeComments(filePath) {
  // In ra đường dẫn file mà script đang kiểm tra
  console.log(`Đường dẫn file cần xử lý: ${filePath}`);

  // Kiểm tra nếu file không tồn tại
  if (!fs.existsSync(filePath)) {
    console.log('File không tồn tại.');
    return;
  }

  // Đọc file ReactJS
  const sourceCode = fs.readFileSync(filePath, 'utf-8');

  // Loại bỏ comment dạng // và /* ... */
  const codeWithoutComments = sourceCode.replace(/\/\/.*$/gm, '')      // Xóa comment dạng // ...
    .replace(/\/\*[\s\S]*?\*\//g, '');  // Xóa comment dạng /* ... */

  // Ghi lại nội dung đã xử lý vào file gốc
  fs.writeFileSync(filePath, codeWithoutComments, 'utf-8');
  console.log(`File ${filePath} đã được xử lý và các comment đã bị loại bỏ.`);
}

// Lấy tên file từ command line argument
const args = process.argv.slice(2);  // Bỏ qua 2 phần tử đầu tiên (node và tên script)
if (args.length === 0) {
  console.log('Vui lòng cung cấp tên file.');
} else {
  const fileName = args[0];
  const filePath = path.resolve(fileName);  // Lấy đường dẫn tuyệt đối của file
  removeComments(filePath);
}
