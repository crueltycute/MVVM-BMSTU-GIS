package com.park.smet_k.bauman_gis;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

public class Route {
    private Integer from;
    private Integer to;

    // TODO(): после алгоритма построения хранить маршрут
//    private string route;
    public Route(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }
    public int getTo() {
        return to;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }
    public void setTo(Integer to) {
        this.to = to;
    }
}

class GridLocation {
    private Integer x;
    private Integer y;

    public GridLocation(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public int compare(GridLocation o) {
//        if (this.getX() < o.getX() && this.getY() < o.getY()) {
//            return 0;
//        } else {
//            return 1;
//        }
        if (this.getX() < o.getX()) {
            return 1;
        } else if (this.getY() < o.getY()) {
            if (this.getX().equals(o.getX()))
                return 1;
            else
                return 0;
        } else {
            return 0;
        }
    }
}

// для приоритетной очередин
@RequiresApi(api = Build.VERSION_CODES.N)
class AStarSearch {
    //    Pair<GridLocation, Double> PQElement;
    private PriorityQueue<Pair<GridLocation, Double>> frontier;

    public static Comparator<Pair<GridLocation, Double>> PQComparator = (c1, c2) -> (int) (c1.second - c2.second);

    AStarSearch() {
        frontier = new PriorityQueue<>(PQComparator);
    }


    public void doAStarSearch(GridWithWeights graph,
                GridLocation start, GridLocation goal,
                Map<GridLocation, GridLocation> came_from, Map<GridLocation, Double> cost_so_far) {
//        frontier = new PriorityQueue<>(PQComparator);
        frontier.add(new Pair<>(start, 0.0));

        came_from.put(start, start);
        cost_so_far.put(start, 0.0);

        Integer counter = 0;

        while (!frontier.isEmpty()) {
            counter += 1;
            GridLocation current = frontier.poll().first;

            if (current == goal) {
                break;
            }

            for (GridLocation next : graph.neighbors(current)) {
                double new_cost = cost_so_far.get(current) + graph.cost(current, next);
                if (!cost_so_far.containsKey(next) || new_cost < cost_so_far.get(next)) {
                    cost_so_far.put(next, new_cost);
                    double priority = new_cost + heuristic(next, goal);
                    frontier.add(new Pair<>(next, priority));
                    came_from.put(next, current);
//                    came_from.put(current, next);
                }
            }
            Log.d("search", Integer.toString(counter));
        }

    }

    public ArrayList<GridLocation> reconstruct_path(GridLocation start, GridLocation goal, Map<GridLocation, GridLocation> came_from) {
        ArrayList<GridLocation> path = new ArrayList<>();
        GridLocation current = goal;

        while (current != start) {
            path.add(current);
            current = came_from.get(current);
        }

        path.add(start);

        return path;
    }


    // евристика для A со звездой
    static double heuristic(GridLocation a, GridLocation b){
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY());
    }
}

class GridWithWeights {
    ArrayList<GridLocation> DIRS;
    Integer width, height;
    Set<GridLocation> walls;

    public GridWithWeights() {
        width = 0;
        height = 0;
        DIRS = new ArrayList<>();
        walls = new TreeSet<>(GridLocation::compare);

        DIRS.add(new GridLocation(1,0));
        DIRS.add(new GridLocation(0, -1));
        DIRS.add(new GridLocation(-1,0));
        DIRS.add(new GridLocation(0,1));
    }

    public GridWithWeights(Integer w, Integer h) {
        width = w;
        height = h;
        DIRS = new ArrayList<>();
        walls = new TreeSet<>(GridLocation::compare);

        DIRS.add(new GridLocation(1,0));
        DIRS.add(new GridLocation(0, -1));
        DIRS.add(new GridLocation(-1,0));
        DIRS.add(new GridLocation(0,1));
    }

    public void add_rect(Integer x1, Integer y1, Integer x2, Integer y2) {
        for (Integer x = x1; x < x2; ++x) {
            for (Integer y = y1; y < y2; ++y) {
                walls.add(new GridLocation(x, y));
            }
        }
    }

    // получить вес перехода от одного узла к другому
    double cost(GridLocation from_node, GridLocation to_node) {
        // тк веса везде одинаковые то 1
        return 1;
    }

    // в границах?
    boolean in_bounds(GridLocation id) {
        return ((0 <= id.getX()) && (id.getX() < this.width) && (0 <= id.getY()) && (id.getY() < this.height));
    }

    // проверка на проходимость
    boolean passable(GridLocation id) {
        return !walls.contains(id);
    }

    // возможные шаги (вправо, вниз, влево, вверх)
    ArrayList<GridLocation> neighbors(GridLocation id) {
        ArrayList<GridLocation> results = new ArrayList<>();

        for (GridLocation dir : DIRS) {
            GridLocation next = new GridLocation(id.getX() + dir.getX(), id.getY() + dir.getY());
            if (in_bounds(next) && passable(next)) {
                results.add(next);
            }
        }

//            if ((id.getX() + id.getY()) % 2 == 0) {
//                // aesthetic improvement on square grids
//                std::reverse(results.begin(), results.end());
//            }

        return results;
    }

}