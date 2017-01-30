#include "doubleLinkedList.h"
#include <iostream>
#include <fstream>
#include <string>
using namespace std;

listNode::listNode()								
{
	word = "";
	next = NULL;
	prev = NULL;
}

listNode::listNode(string w, listNode *n, listNode *p)	
{
	word = w;
	next = n;
	prev = p;
}

void listNode :: setWord(string w)			//sets word to the node
{
	word = w;
}

void listNode::setNext(listNode *n)			//sets the next pointer for the node
{
	next = n;
}

void listNode::setPrev(listNode *p)			//sets the prev pointer for the node
{
	prev = p;
}

string listNode::getWord()					//returns the word from the node
{
	return word;
}

listNode * listNode::getNext()				//returns the next pointer of the node
{
	return next;
}

listNode * listNode::getPrev()				//returns the prev pointer of the node
{
	return prev;
}

listNode::~listNode()						
{
}



doubleLinkedList::doubleLinkedList()
{
	head = NULL;
	end = NULL;
	current = NULL;
}

doubleLinkedList::doubleLinkedList(listNode *h, listNode *e, listNode *c)
{
	head = h;
	end = e;
	current = c;
}

void doubleLinkedList::setHead(listNode *h)		//sets the head pointer for the linked lists
{
	head = h;
}

void doubleLinkedList::setEnd(listNode *e)		//sets the end pointer for the linked lists
{
	end = e;
}

void doubleLinkedList::setCurrent(listNode *c)	//sets the currnet pointer for the linked lists
{
	current = c;
}

listNode* doubleLinkedList::getHead()			//returns the head pointer of the linked list
{
	return head;
}

listNode* doubleLinkedList::getEnd()			//returns the end pointer of the linked list
{
	return end;
}

listNode* doubleLinkedList::getCurrent()		//returns the current pointer of the linked list
{
	return current;
}

void doubleLinkedList::pointToNext()			//move the current pointer forward
{
	current = current->getNext();
}

void doubleLinkedList::pointToPrev()			//moves the current pointer backwards
{
	current = current->getPrev();
}

//------------------------------------------------------
void doubleLinkedList::insert(listNode *newNode, listNode *insertLocation)
//This method inserts a new node into a list after the provided location
//------------------------------------------------------
{
	listNode *temp;
	if (insertLocation == NULL || newNode == NULL)
		return;

	if (insertLocation != end)
	{
		temp = insertLocation->getNext();
		insertLocation->setNext(newNode);
		newNode->setPrev(insertLocation);
		newNode->setNext(temp);
		temp->setPrev(newNode);
		temp = NULL;
	}

	else
	{
		append(newNode);
	}
}

//------------------------------------------------------
void doubleLinkedList::append(listNode *newNode)
//This method takes a node and appends it to the end of the list
//------------------------------------------------------
{
	if (newNode == NULL)
		return;

	//If list is empty
	if (head == NULL && end == NULL)
	{
		head = newNode;
		newNode->setPrev(NULL);
		end = newNode;
		newNode->setNext(NULL);
		current = newNode;
	}

	else
	{
		end->setNext(newNode);
		newNode->setNext(NULL);
		newNode->setPrev(end);
		end = newNode;
	}
}

//------------------------------------------------------
void doubleLinkedList::deleteNode(listNode *deleteLocation)
//This method takes a word and searches through the list in order to delete it
//------------------------------------------------------
{
	listNode *temp;

	if (deleteLocation == NULL)
		return;

	//if the list is empty
	if (head == NULL && end == NULL)
		return;
	else
	{
		if ((head == end) && deleteLocation == head) //if the list has one node
		{
			temp = head;
			head = NULL;
			end = NULL;
			current = NULL;
			delete temp;
		} // end of if the list has one node

		else
		{
			if (deleteLocation == head) // if delete the head
			{
				temp = head;
				head = head->getNext();
				head->setPrev(NULL);
				delete temp;
			}
			else
			{

				if (deleteLocation == end) // if delete the end
				{
					temp = end;
					end = end->getPrev();
					end->setNext(NULL);
					delete temp;
				}

				else // general case
				{
					deleteLocation->getPrev()->setNext(deleteLocation->getNext());
					deleteLocation->getNext()->setPrev(deleteLocation->getPrev());
					delete deleteLocation;
				}

			}

		} // end of the else of 

	} // end of else list is not empty

}

//------------------------------------------------------
void doubleLinkedList::printListForward()
//This method takes in an object of linkedList and prints its elements going forwards
//------------------------------------------------------
{


	if (current != NULL)
	{
		cout << current->getWord() << endl;
		current = current->getNext();
		printListForward();
	}
	

}

//------------------------------------------------------
void doubleLinkedList::printListBackward()
//This method takes an object of type linkedList and prints out its elements going backwards
//------------------------------------------------------
{


	if(current != NULL)
	{
		cout << current->getWord() << endl;
		current = current->getPrev();
		printListBackward();
	}

}

//------------------------------------------------------
listNode* doubleLinkedList::findWord(string theWord)
//This mehtod takes a string and returns the node that corresponds to the word
//------------------------------------------------------
{
	listNode *found = NULL;

	if (current != NULL)
	{
		if (!current->getWord().compare(theWord))
		{
			found = current;
		}
		else
		{
			current = current->getNext();
			found = findWord(theWord);
		}
	}

	return found;
}

//------------------------------------------------------
void doubleLinkedList::clearList()
//This method deletes the linked list
//------------------------------------------------------
{
	while (head != NULL)
		deleteNode(head);
}

//------------------------------------------------------
doubleLinkedList::~doubleLinkedList()
//Destructor
//------------------------------------------------------
{
	while (head != NULL)
		deleteNode(head);
}
