package dev.kr9ly.kolonia.prelude

class Lens<Node, Leaf>(val get: (Node) -> Leaf, val copy: (Node, Leaf) -> Node)