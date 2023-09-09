export type Ingredient = {
    id: number;
    name: string;
}

export type Recipe = {
    id: number;
    name: string;
    description: string;
    ingredients: Array<Ingredient>;
}
