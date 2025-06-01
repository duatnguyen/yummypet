// src/store/store.ts
import { configureStore } from '@reduxjs/toolkit';
import { mainReducer } from '../slices/MainSlice'; // Bạn sẽ tạo slice sau

const store = configureStore({
  reducer: {
    main: mainReducer, // Kết nối reducer vào store
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;
