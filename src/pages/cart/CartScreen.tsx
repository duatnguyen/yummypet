import React, { ForwardedRef, forwardRef } from "react";
import { useLocation, useParams } from "react-router-dom";

interface CartProps {}

export type CartRef = {};
const CartScreen = (props: CartProps, ref: ForwardedRef<CartRef>) => {
  const { id } = useParams();
  const location = useLocation();
  console.log('location', location);
  const { name, age } = location.state || {};

  console.log('name', name);

  React.useImperativeHandle(ref, () => ({}));
  return (
    <div>
      <div>CartScreen</div>
    </div>
  );
};

export default forwardRef(CartScreen);
