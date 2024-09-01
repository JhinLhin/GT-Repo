;;=============================================================
;;  CS 2110 - Spring 2024
;;  Homework 6 - GCD
;;=============================================================
;;  Name: Jinlin Yang
;;============================================================

;;  In this file, you must implement the 'MOD' and 'GCD' subroutines.

.orig x3000
    ;; You do not need to write anything here
    LD R6, STACK_PTR

    ;; Pushes arguments A and B
    ;
    ;
    ;->
    
    ADD R6, R6, -2 ; move up 2 to make room for A and B
    ;->
    ;
    ;
    
    LD R1, A ; load value of A into R1
    STR R1, R6, 0 ; store value of A into 
    ;-> A
    ;
    ;
    
    LD R1, B ; load value of B into R1
    STR R1, R6, 1  ; store value of B into
    ;-> A
    ;-> B
    ;
    
    JSR GCD ; call the subroutine
    
    ; after subroutine finished
    ;->  return value
    ;    argument 1
    ;    argument 2
    ;
    LDR R0, R6, 0 ; store the return value into R0
    ADD R6, R6, 3 ; 
    ;
    ;
    ;
    ;->
    HALT

    STACK_PTR   .fill xF000
    ;; You can change these numbers for debugging!
    A           .fill 10
    B           .fill 4


;;  MOD Pseudocode (see PDF for explanation and examples)   
;;  
;;  MOD(int a, int b) {
;;      while (a >= b) {
;;          a -= b;
;;      }
;;      return a;
;;  }

MOD ;; Do not change this label! Treat this as like the name of the function in a function header
    ;; Code your implementation for the MOD subroutine here!
    
    ;Stack Buildup - callee:MOD(int a, int b)
    ;NOTE: it is the same process every single time
    ;      we can just copy and paste
    
    ADD R6, R6, -4 ; push 4 spaces up, SP initially pointed at the first argument, if we don't have any first argument will hold null
                   ; push 4 up to hold RV, RA, OFP, first LV
    		        ; set return val later
	STR	R7, R6, 2  ; save RA
	STR	R5, R6, 1  ; save old FP
		           ; set local var later, we might store the address of first local here, and compute addresses of others by first + 1
	ADD	R5, R6, 0  ; FP = SP
	ADD	R6, R6, -5 ; push 5 words	
	STR	R0, R5, -1 ; save SR1
	STR	R1, R5, -2 ; save SR2
	STR	R2, R5, -3 ; save SR3
	STR	R3, R5, -4 ; save SR4
	STR	R4, R5, -5 ; save SR5

    ;Implementation of MOD(int a, int b)
    ;Remember we can only use R0 to R4 as GPRs
    
    ;MOD(int a, int b) {
    while1
    ;   while (a >= b) { = a - b >= 0
    ;       find the value of -b and store it into R1
    LDR R1, R5, 5
    NOT R1, R1
    ADD R1, R1, 1
    ;       find the value of a and store it into R0
    LDR R0, R5, 4
    ;       Is it true that a - b >= 0
    ADD R0, R0, R1 ; now R1 = R1 - R0 = a - b
    BRN loopover
    ;       a -= b;
    ;       store the value of R1 into first argument a
    STR R0, R5, 4
    BR while1
    ;   }
    loopover
    ;   return a;
    LDR R0, R5, 4
    STR R0, R5, 3 ; now set return value = a
    ;}
    
    ;Stack Teardown 
    LDR	R4, R5, -5	; restore R4
	LDR	R3, R5, -4	; restore R3
	LDR	R2, R5, -3	; restore R2
	LDR	R1, R5, -2	; restore R1
	LDR	R0, R5, -1	; restore R0
	ADD	R6, R5, 0  	; pop saved regs, and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			    ; MOD() is done!



;;  GCD Pseudocode (see PDF for explanation and examples)
;;
;;  GCD(int a, int b) {
;;      if (b == 0) {
;;          return a;
;;      }
;;        
;;      while (b != 0) {
;;          int temp = b;
;;          b = MOD(a, b);
                ;After JSR-caller
                ;LDR R0, R6, 0 ; store the return value into R0
                ;ADD R6, R6, 3 ; pop RV and 2 arguments off the stack
;;          a = temp;
;;      }
;;      return a;
;;  }

GCD ;; Do not change this label! Treat this as like the name of the function in a function header
    ;; Code your implementation for the GCD subroutine here!
    
    ;Stack Buildup - callee:GCD(int a, int b)
    ;NOTE: it is the same process every single time
    ;      we can just copy and paste
    
    ADD R6, R6, -4 ; push 4 spaces up, SP initially pointed at the first argument, if we don't have any first argument will hold null
                   ; push 4 up to hold RV, RA, OFP, first LV
    		        ; set return val later
	STR	R7, R6, 2  ; save RA
	STR	R5, R6, 1  ; save old FP
		           ; set local var later, we might store the address of first local here, and compute addresses of others by first + 1
	ADD	R5, R6, 0  ; FP = SP
	ADD	R6, R6, -5 ; push 5 words	
	STR	R0, R5, -1 ; save SR1
	STR	R1, R5, -2 ; save SR2
	STR	R2, R5, -3 ; save SR3
	STR	R3, R5, -4 ; save SR4
	STR	R4, R5, -5 ; save SR5
	
	;Implementation of GCD(int a, int b)
    ;Remember we can only use R0 to R4 as GPRs
	;;  GCD(int a, int b) {
;;      if (b == 0) {
        LDR R1, R5, 5 ; R1 = b
        BRZ return
;;          return a;
;;      }
    while
        LDR R1, R5, 5 ; R1 = b
        BRZ return
;;      while (b != 0) {
;;          int temp = b;
        LDR R1, R5, 5 ; R1 = b
        STR R1, R5, 0 ;temp has been stored to location pointed by R5
        
;;          b = MOD(a, b);
    ; we have to load a and b onto top of stack again
    ; a
    ; b
    ; top of the GCD stack
    LDR R0, R5, 4 ; R0 = a
    ADD R6, R6, -2 ; 
    ;-> a
    ;   b
    ;   top of the GCD STACK
    STR R0, R6, 0
    STR R1, R6, 1
    JSR MOD ; MOD(int a, int b)
    LDR R1, R6, 0 ; store the return value into R1
    STR R1, R5, 5 ; store the new value into location of argument 2 b
    ADD R6, R6, 3 ; pop RV and 2 arguments off the stack
;;          a = temp;
    LDR R0, R5, 0
    STR R0, R5, 4 ; store the value of temp into location of argument 1 a
    BR while
;;      }
    return
;;      return a;
        LDR R0, R5, 4 ;R0 = a
        STR R0, R5, 3 ; set return value to a
;;  }
	
	;Stack Teardown 
    LDR	R4, R5, -5	; restore R4
	LDR	R3, R5, -4	; restore R3
	LDR	R2, R5, -3	; restore R2
	LDR	R1, R5, -2	; restore R1
	LDR	R0, R5, -1	; restore R0
	ADD	R6, R5, 0  	; pop saved regs, and local vars
	LDR	R7, R5, 2	; R7 = ret addr
	LDR	R5, R5, 1	; FP = Old FP
	ADD	R6, R6, 3 	; pop 3 words
	RET			    ; GCD() is done!
.end