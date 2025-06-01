import React, { ForwardedRef, forwardRef } from "react";

interface productDetailProps {}

export type productDetailRef = {};
const productDetailScreen = (props: productDetailProps, ref: ForwardedRef<productDetailRef>) => {
  

  React.useImperativeHandle(ref, () => ({}));
  return (
    <div>
      <div>productDetailScreen</div>
    </div>
  );
};

export default forwardRef(productDetailScreen);