package com.example.final1.demo.LinkedList;

public class SingleLinkedListDemo {

  public static void main(String[] args) {
    // 進行測試
    // 先創建節點
    HeroNode hero1 = new HeroNode(1, "宋江", "及時雨");
    HeroNode hero2 = new HeroNode(2, "盧俊毅", "玉麒麟");
    HeroNode hero3 = new HeroNode(3, "吳用", "智多星");
    HeroNode hero4 = new HeroNode(4, "林沖", "豹子頭");

    // 創建要給鏈表
    SingleLinkedList singleLinkedList = new SingleLinkedList();

    // singleLinkedList.add(hero1);
    // singleLinkedList.add(hero2);
    // singleLinkedList.add(hero3);
    // singleLinkedList.add(hero4);

    singleLinkedList.addByOrder(hero1);
    singleLinkedList.addByOrder(hero4);
    singleLinkedList.addByOrder(hero2);
    singleLinkedList.addByOrder(hero3);
    singleLinkedList.addByOrder(hero3);

    singleLinkedList.list();
    // 測試修改節點的代碼
    HeroNode newHeroNode = new HeroNode(2, "小盧", "玉麒麟");
    singleLinkedList.update(newHeroNode);

    System.out.println("修改後的鏈表情況~~");

    singleLinkedList.list();

    // 刪除一個節點
    singleLinkedList.del(1);
    System.out.println("修改後的鏈表情況~~");
    singleLinkedList.list();

    System.out.println(getLength(singleLinkedList.getHead()));
  }

  // 方法: 獲取到單鏈表節點的個數(需求不統計頭節點)
  public static int getLength(HeroNode head) {
    if (head.next == null) {
      return 0;
    }
    int length = 0;
    HeroNode cur = head.next; // 沒有統計頭節點
    while (cur != null) {
      length++;
      cur = cur.next;
    }
    return length;
  }
}

// 定義 SingleLinkedList 管理 HeroNode

class SingleLinkedList {
  // 先初始化一個頭節點，頭節點不要動，不存放具體的數據
  private HeroNode head = new HeroNode(0, "", "");

  // 返回頭節點
  public HeroNode getHead() {
    return head;
  }

  // 添加節點到單向鏈表
  // 思路，當不考慮編號順序時
  // 找到當前鏈表的最後節點
  // 將最後的 next 指向新的節點
  public void add(HeroNode heroNode) {

    // 因為 head 節點不能動，因此我們需要一個輔助遍歷 temp
    HeroNode temp = head;
    // 遍歷鏈表，找到最後
    while (true) {
      if (temp.next == null) {
        break;
      }
      // 如果沒有找到最後，將 temp 后移
      temp = temp.next;
    }
    // 當退出 while 循環時，temp就指向了鏈表的最後
    temp.next = heroNode;

  }

  // 添加英雄時，根據排名將英雄插入到指定位置
  public void addByOrder(HeroNode heroNode) {
    // 因為頭節點不能動，因此我們需要一個輔助變量來遍歷
    // 因為單鏈表，找的 temp 是位於添加位置的前一個節點

    HeroNode temp = head;
    boolean flag = false; // 標示添加的編號是否存在，默認為 false ;
    while (true) {
      if (temp.next == null) {
        break;
      }
      if (temp.next.no > heroNode.no) { // 位置找到，就在 temp 的後面插入
        break;
      } else if (temp.next.no == heroNode.no) { // 說明希望添加 heroNode 已存在

        flag = true; // 說明編號存在
        break;
      }
      temp = temp.next; // 后移，遍歷當前鏈表

    }

    if (flag) { // 不能添加，說明編號存在
      System.out.printf("編號%d已經存在，不能加入\n", heroNode.no);
    } else {
      heroNode.next = temp.next;
      temp.next = heroNode;

    }
  }

  // 修改節點的信息，根據 no 編號來修改，即 no 編號不能改
  // 1.根據 newHeroNode 的 no 來修改即可
  public void update(HeroNode newHeroNode) {
    if (head.next == null) {
      System.out.println("鏈表為空");
      return;
    }
    // 找到需要修改的節點，根據 no 編號
    // 定義一個輔助變量
    HeroNode temp = head.next;
    boolean flag = false;
    while (true) {
      if (temp == null) {
        break; // 已經遍歷完鏈表了
      }
      if (temp.no == newHeroNode.no) {
        flag = true;
        break;
      }
      temp = temp.next;
    }
    // 根據 flag 判斷是否找到要修改的節點
    if (flag) {
      temp.name = newHeroNode.name;
      temp.nickname = newHeroNode.nickname;
    } else {
      System.out.printf("沒有找到編號%d的節點，不能修改", newHeroNode.no);
    }
  }

  // 刪除節點
  // 思路
  // 1.head 不能動 ， 因此需要一個temp輔助節點找到待刪除的前一個節點
  public void del(int no) {
    HeroNode temp = head;
    boolean flag = false; // 標示是否找到待刪除節點的
    while (true) {
      if (temp.next == null) { // 已經到鏈表的最後
        break;
      }
      if (temp.next.no == no) {
        // 找到了待刪除的前一個節點 temp
        flag = true;
        break;
      }
      temp = temp.next; // temp后移，遍歷
    }

    if (flag) { // 找到
      temp.next = temp.next.next;
    } else {
      System.out.printf("要刪除的節點%d不存在", no);
    }

  }

  // 顯示鏈表(遍歷)
  public void list() {
    // 先判斷鏈表是否為空
    if (head.next == null) {
      System.out.println("鏈表為空");
      return;
    }
    // 因為頭節點不能動，因此我們需要一個輔助變量來遍歷
    HeroNode temp = head.next;
    while (true) {
      // 判斷是否到鏈表最後
      if (temp == null) {
        break;
      }
      // 輸出節點信息
      System.out.println(temp);
      // 將 temp 后移
      temp = temp.next;
    }
  }

}

// 定義一個 HeroNode ， 每個 HeroNode 對象就是一個節點

class HeroNode {

  public int no;
  public String name;
  public String nickname;
  public HeroNode next; // 指向下一個節點

  // 構造器
  public HeroNode(int no, String name, String nickname) {
    this.no = no;
    this.name = name;
    this.nickname = nickname;
  }

  // 為了顯示方便，重新 toString
  @Override
  public String toString() {
    return "HeroNode [no=" + no + ", name= " + name + ", nickname= " + nickname + "]";
  }

}
