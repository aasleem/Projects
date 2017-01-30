//Ahmed Sleem

#include <iostream>
#include <string>
#include "doubleLinkedList.h"

//=========================================================================
string removePunctuation(string theWord);
string lowerCase(string theWord);
int partition(int first, int last, string list[]);
void swap(int first, int second, string list[]);
void recQuickSort(int first, int last, string list[]);
void quickSort(string list[], int arraySize);
int binarySearch(string searchWord, string list[], int arraySize);
//=========================================================================


//------------------------------------------------------
void main(int argc, char *argv[])
//------------------------------------------------------
{
	// check for command line arguments
	if (argc != 3)	//no command line parameters (missing file name) or more than one paramater.
	{
		cout << endl << "Correct usage: " << argv[0] << " <filename1> <filename2>" << endl;
		return;
	}
	else			// only two command line parameter.. correct usage
	{
		ifstream dictionaryReader(argv[1]);
		ifstream inputReader(argv[2]);

		if (!dictionaryReader.is_open())
		{
			cout << "Could not open file: " << argv[1] << endl;
			return;
		}
		else if (!inputReader.is_open())
		{
			cout << "Could not open file: " << argv[2] << endl;
			return;
		}
		
//program starts here
	// 1 - Create  word counter called dictionaryCounter
		int dictionaryCounter = 0, wordFound;
		
		string *dictionaryArray;
		doubleLinkedList dictionaryList, inputList, incorrectList;
		string dictionaryWord, currentWord, lowercaseWord;
		listNode *newWord, *inputNode,*incorrectNode;
		
		
	// 2 - Read dictionary file and covert each word to lower case
	// 3 - Each time  read, check if the word is not in the list (unique) then store it and increment dictonaryCounter
		while (dictionaryReader >> currentWord)
		{
			currentWord = removePunctuation(currentWord);
			if (dictionaryList.findWord(currentWord) == NULL)
			{
				newWord = new listNode;
				newWord->setWord(currentWord);
				dictionaryList.append(newWord);
				dictionaryCounter++;
			}
		}
	// 4 - Allocate memory for dictionaryArray, size dictionaryCounter
		dictionaryArray = new string[dictionaryCounter];

	// 5 - Move linked list info to dictionaryArray
	// 6 - Delete linked list
		for (int x = 0; x < dictionaryCounter; x++)
		{
			dictionaryArray[x] = dictionaryList.getHead()->getWord();
			dictionaryList.deleteNode(dictionaryList.getHead());
		}

	// 7 - Sort dictionaryArray using Merge/Heap/Quick Sort
		quickSort(dictionaryArray, dictionaryCounter);

	// 8 - Read the input file
	// 9 - Each time you read, check if the word is not in the list (unique) then store it in the inputList
		while (inputReader >> currentWord)
		{
			currentWord = removePunctuation(currentWord);
			if (inputList.findWord(currentWord) == NULL)
			{
				newWord = new listNode;
				newWord->setWord(currentWord);
				inputList.append(newWord);
			}
		}

	// 10 - After list is full, take an element convert it to lowercase and store it in lowercaseWord
	// 11 - Then search for lowercaseWord in the dictionaryArray
		inputList.setCurrent(inputList.getHead());
		while (inputList.getCurrent() != NULL)
		{
			currentWord = inputList.getCurrent()->getWord();
			lowercaseWord = lowerCase(currentWord);
			wordFound = binarySearch(lowercaseWord, dictionaryArray, dictionaryCounter);

	// 12 - If the lowercaseWord doesn't exist, then store it in a incorrectList
			if (wordFound == -1)
			{
				newWord = new listNode;
				newWord->setWord(currentWord);
	
				incorrectList.setCurrent(incorrectList.getHead());
				if(incorrectList.getHead() == NULL)
					incorrectList.append(newWord);
				else
				{
					incorrectList.setCurrent(incorrectList.getHead());
					while (incorrectList.getCurrent() != NULL )
					{
						if (incorrectList.getCurrent()->getWord() < newWord->getWord())
							incorrectList.pointToNext();
						else
							break;
					}
					if (incorrectList.getCurrent() != NULL)
					{
						incorrectList.pointToPrev();
						if (incorrectList.getCurrent() == NULL)
							incorrectList.addToTop(newWord);
						else
						incorrectList.insert(newWord, incorrectList.getCurrent());
					}

					else
						incorrectList.append(newWord);

				}
			}
			inputList.pointToNext();

	// 13 - Repeat 10-12
		}


	// 14 - Print incorrectList
		incorrectList.setCurrent(incorrectList.getHead());
		incorrectList.printListForward();
		

	// 15 - Release Memory
		inputList.clearList();
		incorrectList.clearList();
		delete []dictionaryArray;
	}
	
}



//------------------------------------------------------
string removePunctuation(string theWord)
//This method takes a string and returns the string without puncuations at the end of it 
//------------------------------------------------------
{
	string puncuations = "`~!@#$%^&*()_-—=+,<.>[]{}\|//;:'?\"";

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

//------------------------------------------------------
string lowerCase(string theWord)
//This method takes a string and returns the lower case of that string
//------------------------------------------------------
{
	for (int i = 0; i < theWord.length(); i++)
	{
		theWord[i] = tolower(theWord[i]);
	}
	return theWord;
}

//------------------------------------------------------
int partition(int first, int last, string list[])
//This method sorts and swaps through subparitions of array to achieve accending order of elements
//------------------------------------------------------
{
	string pivot;
	int index, smallIndex;
	swap(first, (first + last) / 2, list); 
	pivot = list[first];
	smallIndex = first;
	for (index = first + 1; index <= last; index++)
	{
		if (list[index] < pivot)
		{
			smallIndex++;
			swap(smallIndex, index, list);
		}
	}
	swap(first, smallIndex, list);

	return smallIndex;
}

//------------------------------------------------------
void swap(int first, int second, string list[])
//This method swaps the elements from the array 
//------------------------------------------------------
{
	string temp;
	temp = list[first];
	list[first] = list[second];
	list[second] = temp;
}

//------------------------------------------------------
void recQuickSort(int first, int last, string list[])
/*******************************************************
This method subparitions the array by calling parition() to recieve pivotLocations 
then recursively calls recQuickSort() twice, with different bounds
*******************************************************/
//------------------------------------------------------
{
	int pivotLocation;
	if (first < last)
	{
		pivotLocation = partition(first, last, list); 
		recQuickSort(first, pivotLocation - 1, list);
		recQuickSort(pivotLocation + 1, last, list);
	}

}

//------------------------------------------------------
void quickSort(string list[], int arraySize)
//This method is the command handler for quickSort which calls recQuickSort()
//------------------------------------------------------
{
	recQuickSort(0, arraySize - 1, list);
}



//------------------------------------------------------
int binarySearch(string searchWord, string list[], int arraySize)
//This method search for a keyword using binary search
//------------------------------------------------------
{
	int first = 0;
	int last = arraySize - 1;
	int mid;
	bool found = false;

	while (first <= last && !found)
	{
		mid = (first + last) / 2;

		if (!list[mid].compare(searchWord))
			found = true;
		else if (list[mid] > searchWord)
			last = mid - 1;
		else
			first = mid + 1;
	}

	if (found)
		return mid;
	else
		return -1;
}


