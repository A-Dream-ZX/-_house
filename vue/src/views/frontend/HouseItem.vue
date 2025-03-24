<template>
    <div class="house-item" @click="$emit('click')">
      <div class="house-image">
        <img :src="house.imageUrl" :alt="house.title">
      </div>
      <div class="house-info">
        <h3 class="house-title">{{ house.title }}</h3>
        <div class="house-details">
          <span class="room-type">{{ house.roomType }}</span>
          <span class="area">{{ house.area }}㎡</span>
        </div>
        <div class="house-location">{{ house.location }}</div>
        <div class="house-price">¥{{ house.price }}/月</div>
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
    house: {
      type: Object,
      required: true
    }
  });
  </script>
  
  <style scoped>
  .house-item {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: transform 0.3s;
  }
  
  .house-item:hover {
    transform: translateY(-5px);
  }
  
  .house-image {
    width: 100%;
    height: 200px;
    overflow: hidden;
  }
  
  .house-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .house-info {
    padding: 15px;
  }
  
  .house-title {
    margin: 0 0 10px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
  
  .house-details {
    display: flex;
    gap: 10px;
    margin-bottom: 8px;
  }
  
  .room-type, .area {
    color: #606266;
    font-size: 14px;
  }
  
  .house-location {
    color: #909399;
    font-size: 14px;
    margin-bottom: 8px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .house-price {
    color: #f56c6c;
    font-size: 18px;
    font-weight: 600;
  }
  </style>