#ifndef doubleLinkedList_h
#define doubleLinkedList_h

#include <iostream>
#include <fstream>
#include <string>
using namespace std;

class listNode
{
private:
	string word;
	listNode *next;
	listNode *prev;
public:
	listNode();
	listNode(string w, listNode *n, listNode *p);
	void setWord(string w);
	void setNext(listNode *n);
	void setPrev(listNode *p);
	string getWord();
	listNode * getNext();
	listNode * getPrev(); 
	~listNode();
};

class doubleLinkedList
{
private:
	listNode *head;
	listNode *end;
	listNode *current;
	int listSize = 0;
public:
	doubleLinkedList();
	doubleLinkedList(listNode *h, listNode *e, listNode *c);
	void setHead(listNode *h);
	void setEnd(listNode *e);
	void setCurrent(listNode *c);
	listNode* getHead();
	listNode* getEnd();
	listNode* getCurrent();
	void pointToNext();
	void pointToPrev();
	
	void insert(listNode *newNode, listNode *insertLocation);
	void append(listNode *newNode);
	void deleteNode(listNode *deleteLocation);

	void printListForward();
	void printListBackward();
	listNode *findWord(string theWord);
	void clearList();
	void doubleLinkedList::addToTop(listNode *newNode);

	/*void swap(int first, int second);
	void recQuickSort(int first, int last);
	void quickSort(int listSize);
	int partition(int first, int last);*/

	~doubleLinkedList();
	//void releaseMemory(linkedList *list);  The code of this method is going to be in the destructor 
};


#endif