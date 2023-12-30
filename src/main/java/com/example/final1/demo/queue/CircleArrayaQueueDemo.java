package com.example.final1.demo.queue;

import java.util.Scanner;

public class CircleArrayaQueueDemo {

  public static void main(String[] args) {

    System.out.println("測試數組(環形)");

    // 創建一個環形隊列
    CircleArray queue = new CircleArray(4); // 設置 4 ，其隊列最大空間為 3
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

class CircleArray {

  public int maxSize; // 表示數組的最大容量
  private int front; // 隊列頭(指向隊列的第一個元素) default = 0 (初始為零)
  private int rear; // 隊列尾(指向隊列的最後一個元素 的 最後一個位置) default = 0 (初始為零)
  private int[] arr; // 該數據用於存放數據，模擬隊列

  public CircleArray(int arrMaxSize) {
    maxSize = arrMaxSize;
    arr = new int[maxSize];
  }

  public boolean isFull() {
    return (rear + 1) % maxSize == front;

  }

  public boolean isEmpty() {
    return rear == front;
  }

  public void addQueue(int n) {
    // 判斷隊列是否滿
    if (isFull()) {
      System.out.println("隊列滿，不能加入數據");
      return;
    }

    arr[rear] = n;

    rear = (rear + 1) % maxSize;

  }

  // 獲取隊列的數據，出隊列
  public int getQueue() {
    if (isEmpty()) {
      // 通過拋出異常
      throw new RuntimeException("隊列空，不能取數據");
    }

    // 這已需要分析出 front 是指向隊列的第一個元素
    // 1. 先把 front 對應的值保留到一個臨時變量
    // 2. 將 front 后移
    // 3. 將臨時保存的變量返回

    int value = arr[front];
    front = (front + 1) % maxSize;
    return value;
  }

  // 顯示隊列的所有數據
  public void showQueue() {
    // 遍歷
    if (isEmpty()) {
      System.out.println("隊列空的，沒有數據");
    }
    // 思路: 從 front 開始遍歷，遍歷多少元素
    for (int i = front; i < front + size(); i++) {
      System.out.printf("arr[%d]=%d\n", i % maxSize, arr[i % maxSize]);
    }
  }

  // 求出當前隊列有效數據的個數
  public int size() {
    // rear = 2
    // front = 1
    // maxSize = 3
    return (rear + maxSize - front) % maxSize;
  }

  // 顯示隊列的頭數據
  public int headQueue() {
    if (isEmpty()) {
      throw new RuntimeException("隊列空的，沒有數據");
    }
    return arr[front];
  }

}
