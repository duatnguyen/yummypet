import BButton from "../../components/button/BButton";
import BInput from "../../components/input/BInput";
import { _router } from "../../context/routerSingleton";
import { _global } from "../../global";

const HomeScreen = () => {
  const _openCart = () => {
    _router.openCart(1, "Tuấn", 22);
  };

  const show = () => {
    _global.event.alert.current?.show({
      message: "Tuấn",
      duration: 2000,
      type: "success",
    });
  };

  return (
    <>
      <div>
        <BButton
          type="reset"
          label="On Click"
          onClick={show}
          variant="primary"
        />
        <BInput
          placeholder="Bạn đang tìm kiếm?"
          type="password"
          error="Lỗi xảy ra"
          label="Họ và tên"
          required
        />
        <button onClick={_openCart}>OpenCart</button>
      </div>
    </>
  );
};

export default HomeScreen;
