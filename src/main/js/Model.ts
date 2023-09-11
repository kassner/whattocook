export type Ingredient = {
    id: number;
    name: string;
}

export type Recipe = {
    id: number;
    name: string;
    instructions: string;
    ingredients: Array<Ingredient>;
}

export type Event = {
    type: string;
    description: string;
    date: string;
};
