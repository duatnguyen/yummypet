import fs from 'fs';
import path from 'path';

// Lấy thư mục làm việc hiện tại (thư mục gốc của dự án)
const currentDir = process.cwd();

// Lấy tên màn hình từ tham số dòng lệnh
const screenName = process.argv[2];

if (!screenName) {
  console.log('Vui lòng cung cấp tên màn hình.');
  process.exit(1);
}

// Đảm bảo không có phần dư thừa trong đường dẫn
const screenFolder = path.resolve(currentDir, 'src', 'pages', screenName); // Xây dựng đường dẫn từ thư mục gốc
const screenFile = path.resolve(screenFolder, `${screenName}Screen.tsx`);

// Debug đường dẫn để kiểm tra
console.log('Đường dẫn thư mục:', screenFolder);
console.log('Đường dẫn tệp:', screenFile);

// Kiểm tra xem thư mục đã tồn tại chưa, nếu chưa thì tạo mới
if (!fs.existsSync(screenFolder)) {
  fs.mkdirSync(screenFolder, { recursive: true });
}

// Nội dung file React component
const content = `import React, { ForwardedRef, forwardRef } from "react";

interface ${screenName}Props {}

export type ${screenName}Ref = {};
const ${screenName}Screen = (props: ${screenName}Props, ref: ForwardedRef<${screenName}Ref>) => {
  

  React.useImperativeHandle(ref, () => ({}));
  return (
    <div>
      <div>${screenName}Screen</div>
    </div>
  );
};

export default forwardRef(${screenName}Screen);`;

// Tạo file với nội dung đã định sẵn
fs.writeFileSync(screenFile, content);

console.log(`${screenName}Screen đã được tạo thành công tại ${screenFile}`);
