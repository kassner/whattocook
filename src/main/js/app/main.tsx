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
            <div className="hero h-full">
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
        <div className="flex flex-col-reverse gap-8 lg:flex-row lg:gap-2 xl:gap-4">
            <div className="lg:w-48" id="history">
                <h2 className="text-lg">Session history</h2>
                <ol className="divide-y divide-gray-200 dark:divide-gray-200/10">
                    {history.map((element : Event) : any => {
                        return <li className="w-full leading-4 py-2">
                            <p className="font-lg font-medium">{element.description}</p>
                            <p className="text-sm">{element.type}</p>
                            <TimeAgo date={element.date} live={true} minPeriod={10} className="text-sm" />
                        </li>
                    })}
                </ol>
            </div>
            <div className="divider lg:divider-horizontal" />
            <article className="lg:flex-1">
                <h1>{recipe.name}</h1>
                <div className="flex flex-col xl:flex-row">
                    <div className="mb-6 xl:w-64">
                        <h2>Ingredients</h2>
                        <ul className="mt-2 pl-0 list-none leading-9">
                            {recipe.ingredients.map((ingredient : Ingredient) : any => {
                                return <li className="pl-0 flex items-stretch" key={ingredient.id}>
                                    <button className="btn btn-square btn-outline btn-error btn-sm" onClick={() => removeIngredient(ingredient)}>
                                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                                    </button>
                                    <span className="pl-2">{ingredient.name}</span>
                                </li>;
                            })}
                        </ul>
                    </div>
                    <div className="xl:flex-1">
                        <h2>Instructions</h2>
                        <div className="mt-1 text-justify">{nl2p(recipe.instructions)}</div>
                    </div>
                </div>
            </article>
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
