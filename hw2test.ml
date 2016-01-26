let accept_all derivation string = Some (derivation, string)

type english_nonterminals = 
Morpheme | Prefix | Suffix | Content  

let english_grammar = 
(Morpheme, function
  | Morpheme ->
    [[N Content];
     [N Prefix; N Content];
     [N Content; N Suffix];
     [N Prefix; N Content; N Suffix]]
  | Prefix ->
    [[T"co"];
     [T"pre"];
     [T"sub"];
     [T"ex-"];
     [T"anti"];
     [T"auto"]]
  | Suffix ->
    [[T"ing"];
     [T"less"];
     [T"ish"];
     [T"s"]]
  | Content ->
    [[T"law"];
     [T"way"];
     [T"hero"];
     [T"immune"];
     [T"cop"];
     [T"fish"]])

(* this test checks if it actually processes individual
 rules in the right order and if it processes the input
 list from left to right *)
let l1 = ["ing"; "law"; "anti"; "hero"]

let test_1 = 
  ((parse_prefix english_grammar accept_all l1) = None)

let l2 = ["ex-"; "cop"; "s"]

(* this tests if it stops when it finds the first rule that works *)
let test_2 =
  ((parse_prefix english_grammar accept_all l2) = 
   Some ([(Morpheme, [N Prefix; N Content]); 
          (Prefix, [T"ex-"]);
          (Content, [T"cop"])], ["s"]))