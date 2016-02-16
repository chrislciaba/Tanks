
kenken(N, C, T) :-
    length(T, N),
    checkLists(N, T),
    transpose(T, TT),
    checkLists(N, TT),
    fit_constraints(N, C, T).

checkLists(N, []).
checkLists(N, [H|T]) :-
    length(H, N),
    fd_all_different(H),
    checkLists(N, T).

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
      nth(Ith, Mat, Mat1),
      nth(Jth, Mat1, Ret).

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

%%need to write fd_domain, fd_labeling, fd_all_different

is_between(

kenken_testcase(
  6,
  [
   +(11, [1-1, 2-1]),
   /(2, 1-2, 1-3),
   *(20, [1-4, 2-4]),
   *(6, [1-5, 1-6, 2-6, 3-6]),
   -(3, 2-2, 2-3),
   /(3, 2-5, 3-5),
   *(240, [3-1, 3-2, 4-1, 4-2]),
   *(6, [3-3, 3-4]),
   *(6, [4-3, 5-3]),
   +(7, [4-4, 5-4, 5-5]),
   *(30, [4-5, 4-6]),
   *(6, [5-1, 5-2]),
   +(9, [5-6, 6-6]),
   +(8, [6-1, 6-2, 6-3]),
   /(2, 6-4, 6-5)
  ]
).

kenken_test1(
3,
[-(2, 1-1, 1-2),
 /(2, 2-1, 3-1),
 /(3, 2-2, 2-3),
 -(1, 3-2, 3-3)
]).

kenken_test2(
3, 
[+(5, [1-1, 2-1]), 
+(3, [1-2, 2-2]), 
+(4, [3-1, 3-2]), 
+(5, [2-3, 3-3])]).

kenken_test4(
3,
[/(2, 1-1, 1-2),
 -(2, 2-1, 3-1),
 -(1, 2-2, 2-3),
 /(2, 3-2, 3-3)]).

kenken_test5(
4, 
[
+(7, [1-2, 2-2]),
-(3, 1-3, 2-3),
*(6, [1-4, 2-4, 3-4]),
+(7, [2-1, 3-1]),
+(6, [3-2, 3-3, 4-3]),
/(2, 4-1, 4-2)]).

kenken_test6(
2,
[
*(2, [1-1, 1-2]),
*(2, [2-1, 2-2])]).
