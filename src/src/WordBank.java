package src;

// WordBank.java
// Stores all DSA word data organized by category.
// Each word entry is a String array: { "WORD", "Hint description" }

public class WordBank {

    public static final String[] CATEGORY_NAMES = {
        "Stacks", "Queues", "Arrays", "Linked List", "ArrayList"
    };

    public static final String[][] STACKS = {
        { "STACK",     "A LIFO data structure"         },
        { "PUSH",      "Add element to top"            },
        { "POP",       "Remove element from top"       },
        { "PEEK",      "View top without removing"     },
        { "OVERFLOW",  "Stack is full condition"       },
        { "UNDERFLOW", "Stack is empty condition"      },
        { "LIFO",      "Last In First Out"             },
        { "FRAME",     "Stack memory unit"             },
        { "UNDO",      "Common stack use case"         },
        { "RECURSION", "Uses call stack implicitly"    }
    };

    public static final String[][] QUEUES = {
        { "QUEUE",    "FIFO data structure"            },
        { "ENQUEUE",  "Add element to rear"            },
        { "DEQUEUE",  "Remove from front"              },
        { "FRONT",    "Head of the queue"              },
        { "REAR",     "Tail of the queue"              },
        { "FIFO",     "First In First Out"             },
        { "CIRCULAR", "Queue variant, wraps around"    },
        { "PRIORITY", "Queue with ordered access"      },
        { "BUFFER",   "Queue used in streaming"        },
        { "DEQUE",    "Double-ended queue type"        }
    };

    public static final String[][] ARRAYS = {
        { "ARRAY",    "Indexed collection of elements" },
        { "INDEX",    "Position of an element"         },
        { "LENGTH",   "Size of the array"              },
        { "ELEMENT",  "Single item in array"           },
        { "TRAVERSE", "Visit each element"             },
        { "SORT",     "Arrange elements in order"      },
        { "SEARCH",   "Find element in array"          },
        { "MATRIX",   "2D array structure"             },
        { "STATIC",   "Fixed-size array type"          },
        { "DYNAMIC",  "Resizable array type"           }
    };

    public static final String[][] LINKED_LIST = {
        { "NODE",    "Basic unit of linked list"       },
        { "POINTER", "Stores address of next node"     },
        { "HEAD",    "First node in the list"          },
        { "TAIL",    "Last node in the list"           },
        { "SINGLY",  "One direction linked list"       },
        { "DOUBLY",  "Two direction linked list"       },
        { "NEXT",    "Pointer to following node"       },
        { "PREV",    "Pointer to previous node"        },
        { "INSERT",  "Add a new node"                  },
        { "DELETE",  "Remove an existing node"         }
    };

    public static final String[][] ARRAY_LIST = {
        { "ARRAYLIST", "Dynamic resizable array"       },
        { "CAPACITY",  "Internal array size"           },
        { "RESIZE",    "Grow or shrink the list"       },
        { "ITERATOR",  "Object to traverse list"       },
        { "GENERIC",   "Type-safe collection"          },
        { "ADD",       "Append element to list"        },
        { "REMOVE",    "Delete element from list"      },
        { "GET",       "Retrieve by index"             },
        { "SET",       "Update element at index"       },
        { "CONTAINS",  "Check if element exists"       }
    };

    // Returns the word array for a given category index
    public static String[][] getWords(int categoryIndex) {
        switch (categoryIndex) {
            case 0: return STACKS;
            case 1: return QUEUES;
            case 2: return ARRAYS;
            case 3: return LINKED_LIST;
            case 4: return ARRAY_LIST;
            default: return new String[0][0];
        }
    }

    // Returns how many words a category has
    public static int getWordCount(int categoryIndex) {
        return getWords(categoryIndex).length;
    }
}