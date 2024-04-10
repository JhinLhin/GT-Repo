;;=============================================================
;; CS 2110 - Spring 2024
;; Homework 5 - palindromeWithSkips
;;=============================================================
;; Name: Jinlin Yang
;;=============================================================

;;  NOTE: Let's decide to represent "true" as a 1 in memory and "false" as a 0 in memory.
;;
;;  Pseudocode (see PDF for explanation)
;;  Pseudocode values are based on the labels' default values.
;;
;;  String str = "aibohphobia";
;;  char skipChar = mem[mem[CHARADDR]];
;;  int length = 0;
;;  while (str[length] != '\0') {
;;		length++;
;;	}
;; 	
;;	int left = 0;
;;  int right = length - 1;
;;  boolean isPalindrome = true;
;;  while(left < right) {
;;      if (str[left] == skipChar) {
;;          left++;
;;          continue;
;;      }
;;      if (str[right] == skipChar) {
;;          right--;
;;          continue;
;;      }
;;		if (str[left] != str[right]) {
;;			isPalindrome = false;
;;          break;
;;		}
;;
;;		left++;
;;		right--;
;;	}
;;	mem[mem[ANSWERADDR]] = isPalindrome;

.orig x3000
	;; YOUR CODE HERE
	
	;String str = "aibohphobia";
	LD R0, STRING ; address of str[0]
	
	;char skipChar = mem[mem[CHARADDR]];
	LDI R1, CHARADDR
	
	;int length = 0;
	AND R2, R2, #0
	
	;while1 (str[length] != ’\0’)
	while1
	ADD R3, R0, R2 ; this gives us the address of str[0] + length
	LDR R3, R3, #0 ; this gives us the value of str[length]
	BRZ while1over
	;{
	    ADD R2, R2, #1 ; length++;
	    BR while1
	;}
	while1over
	
	;int left = 0;
	AND R3, R3, #0 ; left = R3
	
	;int right = length - 1;
	ADD R4, R2, #-1 ; right = R4
	
	;boolean isPalindrome = true;
	; 1 is true, 0 is false
	AND R5, R5, #0
	ADD R5, R5, #1
	
	; find - skipChar first 
	NOT R1, R1
	ADD R1, R1, #1 ; R1 = -skipChar
	
	while2
	;while2(left < right) => left - right < 0
	;find -right
	NOT R4, R4
	ADD R4, R4, #1
	ADD R6, R3, R4
	BRZP while2over
	;restore right back to positive
	NOT R4, R4
	ADD R4, R4, #1
	;{
	    if1
	    ;if (str[left] == skipChar) 
	    ADD R6, R0, R3 ; address of str[0] + left
	    LDR R6, R6, #0 ; value of str[left]
	    ADD R6, R6, R1 ;str[left] - skipChar
	    BRNP if1over
	    ;{
	        ADD R3, R3, #1
	        BR EndofIteration
	    ;}
	    if1over
	    
	    if2
	    ;if (str[right] == skipChar)
	    ADD R6, R0, R4 ; address of str[0] + right
	    LDR R6, R6, #0 ; value of str[right]
	    ADD R6, R6, R1 ;str[right] - skipChar
	    BRNP if2over
	    ;{
	        ADD R4, R4, #-1
	        BR EndofIteration
	    ;}
	    if2over
	    
	    if3
	    ;if (str[left] != str[right])
	    ADD R6, R0, R3 ; address of str[0] + left
	    LDR R6, R6, #0 ; value of str[left]
	    ADD R7, R0, R4 ; address of str[0] + right
	    LDR R7, R7, #0 ; value of str[right]
	    ;find -str[right]
	    NOT R7, R7
	    ADD R7, R7, #1
	    ;str[left] - str[right] != 0？
	    ADD R6, R6, R7
	    BRZ if3over
	    ;{
	        AND R5, R5, #0
	        BR while2over
	    ;}
	    
	    if3over
	    
	    ADD R3, R3, #1 ; left++;
	    ADD R4, R4, #-1 ; right--;
	    
	    EndofIteration
	    BR while2
	;}
	while2over
    ;mem[mem[ANSWERADDR]] = isPalindrome;
    STI R5, ANSWERADDR
	
	
	HALT

;; Do not change these values!
CHARADDR    .fill x4004
STRING	    .fill x4019
ANSWERADDR 	.fill x5005
.end

;; Do not change any of the .orig lines!

.orig x4004
    .stringz "a"           ;;Feel free to change this char for debugging
.end

.orig x4019				   
	.stringz "aibohphobia" ;; Feel free to change this string for debugging.
.end

.orig x5005
	ANSWER  .blkw 1        ;;Do not change this value
.end