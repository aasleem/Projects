//Ahmed Sleem

#include <iostream>
#include <fstream>
#include <string>
#include "doubleLinkedList.h"
using namespace std;

/*
  1- findWord() is a function in doubleLinkedList.cpp and is recursive

  2- Both PrintListForward() & PrintListBackward() are recursive

  3- intersectionSet() checks to see if you should create an intersection. If so,
     then it calls intersectionRecursion() which recursively searches, creates
	 a double linked list  with as an intersection and returns that list

  4- unionSet() checks to see if you should create a union. If so, then calls calls
     storeRecursion() which recursively stores words from the first linkedList to result Linked list.
	 After it calls unionRecursion which searches through compares the second list and the result list
	 and stores the unique words from the second list
*/


struct oneList
{
	string listName;
	doubleLinkedList *list;
	oneList *nextList;
};

struct listOfLists
{
	oneList *head;
	oneList *end;
	oneList *current;
};

//==========================================================================================
void intersectionSet(string listOne, string listTwo, string listThree, listOfLists *lists); 
void intersectionRecursion(oneList *listOne, oneList *listTwo, oneList *listThree);

void unionSet(string listOne, string listTwo, string listThree, listOfLists *lists);
void unionRecursion(oneList *listOne, oneList *listTwo, oneList *listThree);
void storeRecursion(oneList *listOne, oneList *listThree);

oneList* findList(string listName, listOfLists *lists);

string removePunctuation(string theWord);
void appendToAllLists(oneList *newNode, listOfLists *lists);
void deleteFromListofLists(oneList *deleteLocation, listOfLists *lists);
oneList * isNext(oneList *deleteLocation, listOfLists *lists);


int checkCommand(string command);
void readFile(string fileName, doubleLinkedList *list);
void readCommand(string fileName, string lName, listOfLists *lists);
void insertCommand(string word1, string word2, listOfLists *lists);
void deleteCommand(string word, listOfLists *lists);
void printCommand(int direction, string listN, listOfLists *lists);
void releaseMemory(listOfLists *lists);
//===========================================================================================



//========================================================================
void main(int argc, char *argv[])
//========================================================================
{
	// check for command line arguments
	if (argc != 2)	//no command line parameters (missing file name) or more than one paramater.
	{
		cout << endl << "Correct usage: " << argv[0] << " <filename>" << endl;
		return;
	}
	else			// only one command line parameter.. correct usage
	{
		ifstream commandReader(argv[1]);

		if (!commandReader.is_open())
		{
			cout << "Could not open file: " << argv[1] << endl;
			return;
		}

	//program starts here
		listOfLists allLists;
		allLists.head = NULL;
		allLists.end = NULL;
		allLists.current = NULL;

		string commandWord, nameListOne, nameListTwo, nameListThree, fileName, printType;
		int commandType = 0;
		while (commandReader >> commandWord)
		{
			commandType = checkCommand(commandWord);
			switch (commandType)
			{
			case 1:		//Checks to see if the command is Read (to read an input file to store into list)
				commandReader >> fileName;
				commandReader >> nameListOne;
				readCommand(fileName, nameListOne, &allLists);
				break;
			case 2:		//Checks to see if the command is Intersection
				commandReader >> nameListOne;
				commandReader >> nameListTwo;
				commandReader >> nameListThree;
				intersectionSet(nameListOne, nameListTwo, nameListThree, &allLists);
				break;
			case 3:		//Checks to see if the command is Union
				commandReader >> nameListOne;
				commandReader >> nameListTwo;
				commandReader >> nameListThree;
				unionSet(nameListOne, nameListTwo, nameListThree, &allLists);
				break;
			case 4:		//Checks to see if the command is Print
				commandReader >> nameListOne;
				commandReader >> printType;
				if (!printType.compare("Forward"))
					printCommand(0, nameListOne, &allLists);
				else if (!printType.compare("Backward"))
					printCommand(1, nameListOne, &allLists);
			} //end of switch case

		} //end of while

		commandReader.close();
		
		releaseMemory(&allLists);	

	} // end of (argc !=2) else

} //end of main


//------------------------------------------------------------------------
oneList * isNext(oneList *deleteL, listOfLists *list)
//This method returns the delete location of a node from the list of lists
//------------------------------------------------------------------------
{
	list->current = list->head;
	while (list->current != NULL)
	{
		if (list->current->nextList == deleteL)
			return list->current;
		list->current = list->current->nextList;
	}
}

//------------------------------------------------------------------------
void deleteFromListofLists(oneList *deleteLocation, listOfLists *lists)
//This method deletes from the list of lists given the deleteLocation
//-----------------------------------------------------------------------
{
	oneList *temp;
	oneList *before;

	if (deleteLocation == NULL)
		return;

	//if the list is empty
	if (lists->head == NULL && lists->end == NULL)
		return;
	else
	{
		if ((lists->head == lists->end) && deleteLocation == lists->head) //if the list has one node
		{
			temp = lists->head;
			
			temp->list->clearList();
			delete temp->list;

			lists->head = NULL;
			lists->end = NULL;
			lists->current = NULL;
			delete temp;

		} // end of if the list has one node

		else
		{
			if (deleteLocation == lists->head) // if delete the head
			{
				temp = lists->head;
				temp->list->clearList();
				delete temp->list;

				lists->head = lists->head->nextList;
				delete temp;
			}
			else
			{

				if (deleteLocation == lists->end) // if delete the end
				{
					before = isNext(deleteLocation, lists);
					temp = lists->end;
				
					temp->list->clearList();
					delete temp->list;

					lists->end = before;
					delete temp;
				}

				else // general case
				{
					before = isNext(deleteLocation, lists);
					before->nextList = deleteLocation->nextList;
					
					deleteLocation->list->clearList();
					delete deleteLocation->list;

					delete deleteLocation;
				}

			}

		} // end of the else of 

	} // end of else list is not empty

	
}

//------------------------------------------------------------------------
void intersectionSet(string listOne, string listTwo, string listThree, listOfLists *lists)
/**************************************************************************
This method is the 'Intersection' command handler. It takes the following parameters:
1 - listOne = the name of the first list
2 - listTwo = the name of the second list
3 - listThree = the name of the result list
4 - lists =  the name of the list of lists

It checks to see if listOne and listTwo are legitiamte if so it calls intersectionRecursion()
to process the intersection then stores the result in listThree and append listThree to the list of lists
***************************************************************************/
//------------------------------------------------------------------------
{
	oneList *tempList1;
	oneList *tempList2;
	oneList *storeList;
	oneList *tempReturnList;
	
	tempList1 = findList(listOne, lists);
	tempList2 = findList(listTwo, lists);

	if (tempList1 != NULL && tempList2 != NULL)
	{
		tempReturnList = new oneList;
		tempReturnList->listName = listThree;
		tempReturnList->list = new doubleLinkedList;
		tempReturnList->list->setCurrent(NULL);
		tempReturnList->list->setHead(NULL);
		tempReturnList->list->setEnd(NULL);

		//implement intersection
		//for each node of linkedList, compare it to the whole second list until you find a match

		tempList1->list->setCurrent(tempList1->list->getHead()); //current node of list of tempList 1 is set to head of that list
		intersectionRecursion(tempList1, tempList2, tempReturnList);
		
		storeList = findList(listThree, lists);
		if (storeList != NULL)
			deleteFromListofLists(storeList, lists);

		appendToAllLists(tempReturnList, lists);
	}

}

//------------------------------------------------------------------------
void intersectionRecursion(oneList *listOne, oneList *listTwo, oneList *listThree)
// This method returns a list of common items in the two lists by recursively searching for similar words 
//------------------------------------------------------------------------
{
	string currentWordA, currentWordB;
	listNode *wordNode, *foundNode, *foundInC;

	if (listOne->list->getCurrent() != NULL)
	{
		currentWordA = listOne->list->getCurrent()->getWord();

		//find current A in B
		listTwo->list->setCurrent(listTwo->list->getHead()); // list 2 current = head
		foundNode = listTwo->list->findWord(currentWordA); //find word from list 1 in list 2
		if (foundNode != NULL)
		{
			listThree->list->setCurrent(listThree->list->getHead()); //list 3 current = head
			foundInC = listThree->list->findWord(currentWordA); //find word from list 2 in list 3
			if (foundInC == NULL)
			{
				wordNode = new listNode;
				wordNode->setNext(NULL);
				wordNode->setPrev(NULL);
				wordNode->setWord(listOne->list->getCurrent()->getWord());
				listThree->list->append(wordNode);
			}
		}

		listOne->list->pointToNext();
		intersectionRecursion(listOne, listTwo, listThree);
	}

}

//------------------------------------------------------------------------
void unionSet(string listOne, string listTwo, string listThree, listOfLists *lists)
/*************************************************************************
This method is the 'Union' command handler. It takes parameters:
1 - listOne = the name of the first list
2 - listTwo = the name of the second list
3 - listThree = the name of the result list
4 - lists =  the name of the list of lists

It checks to see if listOne and listTwo are legitiamte if so it calls storeRecursion() which stores elements 
from listOne to listThree(result list) then calls unionRecursion() which compares listTwo to listThree and stores unique 
elements of listTwo in listThree then listThree is appeneded to the list of lists
*************************************************************************/
//------------------------------------------------------------------------
{
	oneList *tempList1;
	oneList *tempList2;
	oneList *storeList;
	oneList *tempReturnList, *tempList3Return;
	string currentWordA, currentWordB;
	listNode *wordNode, *foundNode, *foundInC;

	tempList1 = findList(listOne, lists);
	tempList2 = findList(listTwo, lists);

	if (tempList1 != NULL && tempList2 != NULL)
	{
		tempReturnList = new oneList;
		tempReturnList->listName = listThree;
		tempReturnList->list = new doubleLinkedList;
		tempReturnList->list->setCurrent(NULL);
		tempReturnList->list->setHead(NULL);
		tempReturnList->list->setEnd(NULL);

		//STORE RECURSION
		tempList1->list->setCurrent(tempList1->list->getHead()); // current = head
		storeRecursion(tempList1, tempReturnList);


		//UNION RECURSION
		tempList2->list->setCurrent(tempList2->list->getHead()); // current = head
		unionRecursion(tempList1, tempList2, tempReturnList);


		storeList = findList(listThree, lists);
		if (storeList != NULL)
			deleteFromListofLists(storeList, lists);

		appendToAllLists(tempReturnList, lists);

	}

}

//------------------------------------------------------------------------
void storeRecursion(oneList *listOne, oneList *listThree)
//This method stores all elements from listOne into listThree
//------------------------------------------------------------------------
{
	string currentWordA, currentWordB;
	listNode *wordNode, *foundNode, *foundInC;

	if (listOne->list->getCurrent() != NULL)
	{
		currentWordA = listOne->list->getCurrent()->getWord();
		listThree->list->setCurrent(listThree->list->getHead()); //list 3 current = head

		foundInC = listThree->list->findWord(currentWordA); //find word from list 2 in list 3

		if (foundInC == NULL)
		{
			// allocate memory for a new node 
			wordNode = new listNode;
			wordNode->setNext(NULL);
			wordNode->setPrev(NULL);

			// copy from the current node of the list to the newNode
			wordNode->setWord(currentWordA);

			// append to the end of the output list
			listThree->list->append(wordNode);
		}

		// move current pointer 
		listOne->list->pointToNext();
		storeRecursion(listOne, listThree);
	}
	//return listThree;
}

//------------------------------------------------------------------------
void unionRecursion(oneList *listOne, oneList *listTwo, oneList *listThree)
//This method compares elements from listTwo with elements from listThree and stores unique elements from listTwo into listThree
//------------------------------------------------------------------------
{
	string currentWordA, currentWordB;
	listNode *wordNode, *foundNode, *foundInC;

	while (listTwo->list->getCurrent() != NULL)
	{
		currentWordB = listTwo->list->getCurrent()->getWord();
		foundNode = listOne->list->findWord(currentWordB);
		if (foundNode == NULL)
		{
			listThree->list->setCurrent(listThree->list->getHead()); //list 3 current = head
			foundInC = listThree->list->findWord(currentWordB); //find word from list 2 in list 3
			if (foundInC == NULL)
			{
				// allocate memory for a new node 
				wordNode = new listNode;
				wordNode->setNext(NULL);
				wordNode->setPrev(NULL);

				// copy from the current node of the list to the newNode
				wordNode->setWord(listTwo->list->getCurrent()->getWord());

				// append to the end of the output list
				listThree->list->append(wordNode);
			}

		}

		// move current pointer 
		listTwo->list->pointToNext();
	}

	//return listThree;

}

//------------------------------------------------------------------------
oneList* findList(string listN, listOfLists *lists)
//This method searches for a list inside the list of lists
//------------------------------------------------------------------------
{
	oneList *found = NULL;
	lists->current = lists->head;
	while (lists->current != NULL)
	{
		if (!lists->current->listName.compare(listN))
		{
			found = lists->current;
			break;
		}
		else
			lists->current = lists->current->nextList;
	}

	return found;
}

//------------------------------------------------------------------------
void readCommand(string fileName, string lName, listOfLists *lists)
/*************************************************************************
This method is the 'Read' command handler. It takes the following parameters:
1 - fileName = the name of the file being read
2 - lName= the name of the list that will hold contents of the file
3 - lists = a pointer to the list of all the lists held before

It calls readFile() to process the reading and calls append
***************************************************************************/
//------------------------------------------------------------------------
{
	oneList *newNode;
	doubleLinkedList *wordList;
	ifstream reader(fileName);

	if (!reader.is_open())
	{
		cout << "Could not open file: " << fileName << endl;
		return;
	}
	reader.close();

	newNode = findList(lName, lists);

	if (newNode != NULL)
		deleteFromListofLists(newNode, lists);

	newNode = new oneList;
	newNode->listName = lName;
	newNode->nextList = NULL;

	wordList = new doubleLinkedList;
	newNode->list = wordList;

	readFile(fileName, newNode->list);

	appendToAllLists(newNode, lists);
}

//------------------------------------------------------------------------
void readFile(string fileName, doubleLinkedList *lists)
//This method opens an input file then stores all words from the file to a linked list
//------------------------------------------------------------------------
{
	listNode *newWord;
	string currentWord;
	ifstream reader(fileName);

	if (!reader.is_open())
	{
		cout << "Could not open file: " << fileName << endl;
		return;
	}

	while (reader >> currentWord)
	{
		currentWord = removePunctuation(currentWord);
		newWord = new listNode;
		newWord->setWord(currentWord);
		lists->append(newWord);
	}

	reader.close();
}

//------------------------------------------------------------------------
void insertCommand(string word1, string word2, doubleLinkedList *lists)
//This method inserts word1 after the location of word2
//------------------------------------------------------------------------
{
	listNode *temp, *insertLocation;
	insertLocation = lists->findWord(word2);
	if (insertLocation != NULL)
	{
		temp = new listNode;
		temp->setWord(word1);
		lists->insert(temp, insertLocation);
	}
}

//------------------------------------------------------------------------
void deleteCommand(string word, doubleLinkedList *lists)
//This method searches for given word then deletes it
//------------------------------------------------------------------------
{
	listNode *deleteN;
	deleteN = lists->findWord(word);
	lists->deleteNode(deleteN);
}

//------------------------------------------------------------------------
void printCommand(int direction, string listN, listOfLists *lists)
/*************************************************************************
This method is the 'Print' command handler. It takes the following parameters:
1 - direction = an integer which determines whether to print forward or backward
2 - lName= the name of the list that will hold contents of the file
3 - lists = a pointer to the list of all the lists held before
***************************************************************************/
//------------------------------------------------------------------------
{
	oneList *temp = findList(listN, lists);
	if (temp != NULL)
	{
		if (direction == 0)
		{
			temp->list->setCurrent(temp->list->getHead());
			temp->list->printListForward();
		}
		else if (direction == 1)
		{
			temp->list->setCurrent(temp->list->getEnd());
			temp->list->printListBackward();
		}
	}
	else
		cout << "Doesn't Exist" << endl;
}

//------------------------------------------------------------------------
int checkCommand(string command)
//This method checks the command read from main method then returns a corresponding code
//------------------------------------------------------------------------
{
	int code = 0;
	if (!command.compare("Read"))
		code = 1;
	else if (!command.compare("Intersection"))
		code = 2;
	else if (!command.compare("Union"))
		code = 3;
	else if (!command.compare("Print"))
		code = 4;

	return code;
}

//------------------------------------------------------------------------
string removePunctuation(string theWord)
//This method takes a string and returns the string without puncuations at the end of it 
//------------------------------------------------------------------------
{
	string puncuations = "`~!@#$%^&*()_-=+[]{}\|//;:'";

	char firstCharacter;
	char lastCharacter;
	int found;
	while (true) //remove puncuations from the end
	{
		lastCharacter = theWord[theWord.length() - 1];
		found = puncuations.find(lastCharacter);

		if (found == std::string::npos)
			break;
		else
		{
			theWord = theWord.substr(0, theWord.length() - 1);
		}
	}

	while (true) //remove puncuations from the beginning 
	{
		firstCharacter = theWord[0];
		found = puncuations.find(firstCharacter);

		if (found == std::string::npos)
			break;
		else
		{
			theWord = theWord.substr(1, theWord.length());
		}

	}
	return theWord;
}

//------------------------------------------------------------------------
void appendToAllLists(oneList *newNode, listOfLists *lists)
//This method takes a node and appends it to the end of the list
//------------------------------------------------------------------------
{
	if (newNode == NULL)
		return;

	//If list is empty
	if (lists->head == NULL && lists->end == NULL)
	{
		lists->head = newNode;
		lists->end = newNode;
		newNode->nextList = NULL;
		lists->current = newNode;
	}

	else
	{
		lists->end->nextList = newNode;
		newNode->nextList = NULL;
		lists->end = newNode;
	}
}

//------------------------------------------------------------------------
void releaseMemory(listOfLists *lists)
//Releases memory and takes care of memory leak
//------------------------------------------------------------------------
{
	while (lists->head != NULL)
		deleteFromListofLists(lists->head, lists);
}
