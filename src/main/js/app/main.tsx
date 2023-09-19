import { createRoot } from 'react-dom/client';
import { useState } from 'react';
import { Recipe, Ingredient, Event } from './Model';
import './main.scss';
import TimeAgo from 'react-timeago';

const App = () => {
    const [recipe, setRecipe] = useState<Recipe|null>(null);
    const [history, setHistory] = useState<Event[]>([]);

    const loadRecipeClick = () => {
        fetch('/session', {method: 'POST'})
            .then(async (response) => {
                if (response.status > 399) {
                    response.json()
                        .then((data) => {
                            if (data.message != undefined) {
                                alert(data.message);
                            } else {
                                alert("Error creating session");
                            }
                        }).catch(() => {
                            alert("Error creating session");
                        })
                    ;
                } else {
                    loadRecipe();
                    loadHistory();
                }
            })
            .catch((e) => {
                console.log(e);
                alert('/session error: ' + e);
            })
        ;
    };

    const loadRecipe = () => {
        fetch('/session/recipe')
            .then(response => response.json())
            .then((data) => {
                const recipe : Recipe = data;
                setRecipe(recipe);
            })
            .catch((e) => {
                console.log(e);
                alert('/session/recipe error: ' + e);
            })
        ;
    };

    const loadHistory = () => {
        fetch('/session/history')
            .then(response => response.json())
            .then((data) => {
                const history : Event[] = data;
                setHistory(history);
            })
            .catch((e) => {
                console.log(e);
                alert('/session/history error: ' + e);
            })
        ;
    };

    const removeIngredient = (ingredient: Ingredient) => {
        fetch('/session/ingredient/' + ingredient.id, {method: 'DELETE'})
            .then(response => response.json())
            .then((data) => {
                if (data !== true) {
                    alert('Error: ' + data.message);
                    return;
                }

                loadRecipe();
                loadHistory();
            })
            .catch((e) => {
                console.log(e);
                alert('error' + e);
            })
        ;
    };

    if (!recipe) {
        return (
            <div className="hero min-h-screen">
                <div className="hero-content text-center">
                    <div className="max-w-md">
                        <h1 className="text-5xl font-bold">What to cook today?</h1>
                        <p className="py-6">No decision fatigue, just get a recipe. Don't have an ingredient at home? Just remove it and you'll get a new recipe.</p>
                        <button className="btn btn-primary" onClick={loadRecipeClick}>Give me a recipe</button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="container mx-auto pt-2">
            <div className="grid grid-cols-6 gap-2 flex items-stretch">
                <div className="border-gray-100 shadow rounded-lg bg-white">
                    <div className="w-full p-2">
                        <h2 className="mt-0 text-xl pb-2 text-zinc-500">Session history</h2>
                        {history.map((element : Event) : any => {
                            return <>
                                <div className="divider mt-0 mb-0 divider-gray-100" />
                                <div className="card w-full">
                                    <div className="card-body p-0 mt-3 leading-4">
                                        {element.description && <p className="font-lg font-medium">{element.description}</p>}
                                        <p className="text-zinc-500 font-sm">{element.type}</p>
                                        <TimeAgo date={element.date} live={true} minPeriod={10} className="text-sm" />
                                    </div>
                                </div>
                            </>
                        })}
                    </div>
                </div>
                <div className="col-span-5 card w-full border-gray-100 shadow rounded-lg bg-white">
                    <div className="card-body p-5">
                        <h1 className="mt-0 text-4xl pb-2 text-zinc-950">{recipe.name}</h1>
                        <div className="grid-cols-12 grid gap-4">
                            <div className="col-span-3">
                                <h2 className="mt-0 text-2xl pb-2 text-zinc-600">Ingredients</h2>
                                <ul className="pl-0 list-none leading-9">
                                    {recipe.ingredients.map((ingredient : Ingredient) : any => {
                                        return <li className="pl-0 flex items-stretch" key={ingredient.id}>
                                            <button className="btn btn-square btn-outline btn-error btn-sm" onClick={() => removeIngredient(ingredient)}>
                                                <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                                            </button>
                                            <span className="pl-2 name">{ingredient.name}</span>
                                        </li>;
                                    })}
                                </ul>
                            </div>
                            <div className="col-span-9 mt-0">
                                <h2 className="mt-0 text-2xl pb-2 text-zinc-600">Instructions</h2>
                                <div className="text-justify">{nl2p(recipe.instructions)}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

const nl2p = (text: string) => {
    return text.split("\n").map(function(item, key) {
        // @TODO remove non-printable characters, i.e.: \r
        if (item.length == 0) {
            return;
        }

        return (<p>{item}</p>);
    });
};

const root = createRoot(document.getElementById("app") as HTMLElement);
root.render(<App />);
