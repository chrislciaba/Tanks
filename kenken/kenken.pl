%% from SWIs clpfd

transpose([], []).
transpose([F|Fs], Ts) :-
    transpose(F, [F|Fs], Ts).

transpose([], _, []).
transpose([_|Rs], Ms, [Ts|Tss]) :-
        lists_firsts_rests(Ms, Ts, Ms1),
        transpose(Rs, Ms1, Tss).

lists_firsts_rests([], [], []).
lists_firsts_rests([[F|Os]|Rest], [F|Fs], [Os|Oss]) :-
        lists_firsts_rests(Rest, Fs, Oss).


%%%%%%%%%%%%%%%%%%%%%%
%%%%%%% PART 1 %%%%%%%
%%%%%%%%%%%%%%%%%%%%%%

%% took 1 ms for 4x4 test case.

kenken(N, C, T) :-
    length(T, N),
    checkLists(N, T),
    transpose(T, TT),
    checkLists(N, TT),
    fit_constraints(N, C, T).

%% just checks if they are all different and have length n
checkLists(N, []).
checkLists(N, [H|T]) :-
    length(H, N),
    fd_all_different(H),
    checkLists(N, T).

%% handles the constraints for lists with empty constraints and ones with
%% lists full of constraints. There is some double checking here
fit_constraints(N, [], []).
fit_constraints(N, [], [H|T]) :-
    fd_domain(H, 1, N),
    fd_labeling(H, []),
    fit_constraints(N, [], T).
fit_constraints(N, [H|T], Res) :-
    find_constr(N, H, Res),
    fit_constraints(N, T, Res).

find_constr(N, +(Tot, List), Res) :- add(N, Tot, List, Res, 0).
find_constr(N, *(Tot, List), Res) :- mult(N, Tot, List, Res, 1).
find_constr(N, -(Tot, X, Y), Res) :- subtr(N, Tot, X, Y, Res).
find_constr(N, /(Tot, X, Y), Res) :- divi(N, Tot, X, Y, Res).

get_entry(Ith-Jth, Mat, Ret) :-
      nth(Ith, Mat, Vec1),
      nth(Jth, Vec1, Ret).

add(N, Tot, [], Res, Tot).
add(N, Tot, [H|T], Res, Sum) :-
    get_entry(H, Res, X),
    fd_domain(X, 1, N),
    fd_labeling(X, []),
    NewSum #= Sum + X,
    add(N, Tot, T, Res, NewSum).

mult(N, Tot, [], Res, Tot).
mult(N, Tot, [H|T], Res, Mult) :-
    get_entry(H, Res, X),
    fd_domain(X, 1, N),
    fd_labeling(X, []),
    NewMult #= Mult * X,
    mult(N, Tot, T, Res, NewMult).

subtr(N, Tot, A, B, Res) :-
    get_entry(A, Res, X),
    fd_domain(X, 1, N),
    fd_labeling(X, []),
    get_entry(B, Res, Y),
    fd_domain(Y, 1, N),
    fd_labeling(Y, []),
    (Tot #= X - Y; Tot #= Y - X).

divi(N, Tot, A, B, Res) :-
     get_entry(A, Res, X),
     fd_domain(X, 1, N),
     fd_labeling(X, []),
     get_entry(B, Res, Y),
     fd_domain(Y, 1, N),
     fd_labeling(Y, []),
     (Tot #= X / Y; Tot #= Y / X).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%% PART 2 %%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% took 1598 ms on the 4x4 test case

plain_kenken(N, C, T) :-
     length(T, N),
     check_grid_p(N, T),
     transpose(T, TTrans),
     arrs_unique_p(TTrans),
     fit_constraints_p(C, T).


check_grid_p(N, []).
check_grid_p(N, [H|T]) :-
     add_nums_p(N, A),
     permutation(A, H),
     check_grid_p(N, T).

%% since we know all values are distinct here, no need to check if the rows
%% are unique.
add_nums_p(0, X).
add_nums_p(N, X) :-
     length(X, N),
     D #= N - 1,
     append(List, [N], X),
     add_nums_p(D, List).

arrs_unique_p([]).
arrs_unique_p([H|T]) :-
     unique_p(H),
     arrs_unique_p(T).

unique_p([]).
unique_p([H|T]) :-
     \+(member(H, T)),
     unique_p(T).

fit_constraints_p([], Res).
fit_constraints_p([H|T], Res) :-
     find_constr1(H, Res),
     fit_constraints_p(T, Res).

find_constr1(+(Tot, List), Res) :- add1(Tot, List, Res, 0).
find_constr1(*(Tot, List), Res) :- mult1(Tot, List, Res, 1).
find_constr1(-(Tot, X, Y), Res) :- subtr1(Tot, X, Y, Res).
find_constr1(/(Tot, X, Y), Res) :- divi1(Tot, X, Y, Res).

add1(Tot, [], Res, Tot).
add1(Tot, [H|T], Res, Sum) :-
   get_entry(H, Res, X),
   NewSum #= Sum + X,
   add1(Tot, T, Res, NewSum).

mult1(Tot, [], Res, Tot).
mult1(Tot, [H|T], Res, Mult) :-
   get_entry(H, Res, X),
   NewMult #= Mult * X,
   mult1(Tot, T, Res, NewMult).

subtr1(Tot, A, B, Res) :-
   get_entry(A, Res, X),
   get_entry(B, Res, Y),
   (Tot #= X - Y; Tot #= Y - X).

divi1(Tot, A, B, Res) :-
    get_entry(A, Res, X),
    get_entry(B, Res, Y),
    (Tot #= X / Y; Tot #= Y / X).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%% PART 3 %%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% noop_kenken(N, C, T) API:
%% Parameters:
%% N: the length of each side of the grid
%% C: The list of constraints
%% T: the solution you would like to have
%%
%% Description of functionality:
%% The way I would do this would be similar to the way I approached part 2 of
%% this project. The predicates would be largely the same until the last part
%% where the constraints are applied. The predicates would be: the initial list
%% must be of length N, the list must contain lists of length N with any
%% permutation of values from 1 to N, if the array is transposed, the resulting
%% lists must all contain unique values. For the constraints, the first place
%% predicate would be that if a constraint includes more than 2 elements, then
%% it must either be the operations of times or plus on all of the numbers.
%% A constraint with 2 elements that it can perform an operation on can be
%% +, -, *, or /. Then you would essentially have to try every possible
%% permutation of the possible operations on every grid that was made
%% according to the constraints described above until you either get an answer,
%% you run out of resources, or you do not find any answer.
%%
%% Returns: either a filled in grid of the solution or "no".
