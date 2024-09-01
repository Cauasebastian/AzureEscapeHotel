// src/store/roomsSlice.js
import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { getAllRooms } from '../Utils/ApiFunctions';

// Async thunk para buscar os quartos
export const fetchRooms = createAsyncThunk('rooms/fetchRooms', async () => {
  const response = await getAllRooms();
  return response;
});

const roomsSlice = createSlice({
  name: 'rooms',
  initialState: {
    rooms: [],
    isLoading: false,
    errorMessage: '',
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchRooms.pending, (state) => {
        state.isLoading = true;
        state.errorMessage = '';
      })
      .addCase(fetchRooms.fulfilled, (state, action) => {
        state.rooms = action.payload;
        state.isLoading = false;
      })
      .addCase(fetchRooms.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        state.isLoading = false;
      });
  },
});

export default roomsSlice.reducer;
