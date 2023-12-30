package com.example.final1.demo.queue;

import java.util.Scanner;

public class ArrayQueueDemo {

  public static void main(String[] args) {
    // 創建一個隊列
    ArrayQueue queue = new ArrayQueue(3);
    char key = ' '; // 接收用戶輸入
    Scanner scanner = new Scanner(System.in);
    boolean loop = true;

    while (loop) {
      System.out.println("s(show): 顯示隊列");
      System.out.println("e(exit): 退出程序");
      System.out.println("a(add): 添加數據到隊列");
      System.out.println("g(get): 從隊列取出數據");
      System.out.println("h(head): 查看隊列頭的數據");

      key = scanner.next().charAt(0);
      switch (key) {
        case 's':
          queue.showQueue();
          break;
        case 'a':
          System.out.println("輸入一個數字");
          int value = scanner.nextInt();
          queue.addQueue(value);
          break;
        case 'g': // 取出數據
          try {
            int res = queue.getQueue();
            System.out.printf("取出的數字是%d\n", res);
          } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
          }

          break;
        case 'h': // 查看隊列頭的數據
          try {
            int res = queue.headQueue();
            System.out.printf("隊列頭的數據是%d\n", res);
          } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
          }
          break;

        case 'e': // 退出
          scanner.close();
          break;

        default:
          break;
      }
    }
    System.out.println("程序退出");
  }

}

class ArrayQueue {

  public int maxSize; // 表示數組的最大容量
  private int front; // 隊列頭
  private int rear; // 隊列尾
  private int[] arr; // 該數據用於存放數據，模擬隊列

  // 創建隊列的構造器
  public ArrayQueue(int arrMaxSize) {

    maxSize = arrMaxSize;
    arr = new int[maxSize];
    front = -1; // 指向隊列頭的前一個位置
    rear = -1; // 指向隊列最後一個數據

  }

  // 判斷隊列是否滿
  public boolean isFull() {
    return rear == maxSize - 1;
  }

  // 判斷隊列是否為空
  public boolean isEmpty() {
    return rear == front;
  }

  // 添加數據到隊列
  public void addQueue(int n) {
    // 判斷隊列是否滿
    if (isFull()) {
      System.out.println("隊列滿，不能加入數據");
      return;
    }
    rear++; // 讓 rear 后移
    arr[rear] = n;

  }

  // 獲取隊列的數據，出隊列
  public int getQueue() {
    if (isEmpty()) {
      // 通過拋出異常
      throw new RuntimeException("隊列空，不能取數據");
    }

    front++; // front 后移
    return arr[front];
  }

  // 顯示隊列的所有數據
  public void showQueue() {
    // 遍歷
    if (isEmpty()) {
      System.out.println("隊列空的，沒有數據");
    }
    for (int i = 0; i < arr.length; i++) {
      System.out.printf("arr[%d]=%d\n", i, arr[i]);
    }
  }

  // 顯示隊列的頭數據
  public int headQueue() {
    if (isEmpty()) {
      throw new RuntimeException("隊列空的，沒有數據");
    }
    return arr[front + 1];
  }
}