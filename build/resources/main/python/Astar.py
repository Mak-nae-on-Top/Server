import sys

from PIL.Image import Image
import cv2 as cv
import json
import io
import base64
from PIL import ImageFile

ImageFile.LOAD_TRUNCATED_IMAGES = True
UL = sys.argv[1]
RL = sys.argv[2]


def read_img(file_path):
    temp = [] #
    blueprint = [] #0 is path, 1is block
    base64_data = open(file_path,'r').read()
    b = base64.b64decode(base64_data)
    img = cv.imread(io.BytesIO(b))
    Img = img.tolist()
    image1 = Image.open(io.BytesIO(b))
    X,Y = image1.size[0], image1.size[1]
    for y in range(Y):
        for x in range(X):
            if Img[y][x] == [255,255,255]:
                temp.append(0)
        else:
                temp.append(1)
        blueprint.append(temp)
    temp = []
    return blueprint


class Node:
    def __init__(self, parent=None, position=None):
        self.parent = parent
        self.position = position
        self.g = 0
        self.h = 0
        self.f = 0

    def __eq__(self, other):
        return self.position == other.position


def heuristic(node, goal):  
    dx = abs(node.position[0] - goal.position[0])
    dy = abs(node.position[1] - goal.position[1])
    return dx + dy


def UseRomListist(Ustr):
    User_List = []
    Ustr = Ustr.replace('[','')
    Ustr = Ustr.replace(']','')
    Ustr = Ustr.split(',')
    for i in Ustr:
        User_List.append(tuple(map(int,i.split(':'))))
    return User_List


def RoomList(Rstr):
    Room_List = []
    Rstr = Rstr.replace('[','')
    Rstr = Rstr.replace(']','')
    Rstr = Rstr.split(',')
    for i in Rstr:
       Room_List.append(tuple(map(int,i.split(':'))))
    return Room_List


def Astar(maze, start, end):
    # Create start and end node
    startNode = Node(None, start)
    endNode = Node(None, end)
     # Initialize both open and closed list
    openList = []
    closedList = []
    # Add the start node
    openList.append(startNode)
    #Loop until you find the end
    while openList:
        # Get the current node
        currentNode = openList[0]
        currentIdx = 0
        for index, item in enumerate(openList):
            if item.f < currentNode.f:
                currentNode = item
                currentIdx = index
        # Pop current off open list, add to closed list
        openList.pop(currentIdx)
        closedList.append(currentNode)
        # Found the goal
        if currentNode == endNode:
            path = []
            current = currentNode
            while current is not None:
                path.append(current.position)
                current = current.parent
            return path[::-1]  # reverse
         # Generate children
        children = []
        # 인접한 xy좌표 전부
        for newPosition in [(0, -1), (0, 1), (-1, 0), (1, 0), (-1, -1), (-1, 1), (1, -1), (1, 1)]:
            # Get node position

            nodePosition = (
                currentNode.position[0] + newPosition[0],  # X
                currentNode.position[1] + newPosition[1])  # Y       
            within_range_criteria = [
                nodePosition[0] > (len(maze) - 1),
                nodePosition[0] < 0,
                nodePosition[1] > (len(maze[len(maze) - 1]) - 1),
                nodePosition[1] < 0,
            ]
            if any(within_range_criteria):  # 하나라도 true면 범위 밖임
                continue
            # 장애물이 있으면 다른 위치 불러오기
            if maze[nodePosition[0]][nodePosition[1]] != 0:
                continue
            new_node = Node(currentNode, nodePosition)
            children.append(new_node)
        # loop all children
        for child in children:
            if child in closedList:
                continue
            # update value f, g, h
            child.g = currentNode.g + 1
            child.h = heuristic(child, endNode)
            child.f = child.g + child.h
            if len([openNode for openNode in openList
                    if child == openNode and child.g > openNode.g]) > 0:
                continue
            openList.append(child)


def Compare():
    RomList  = RoomList(RL)
    end = RomList[0][0],RomList[0][1]
    userX,userY = UseRomListist(UL)[0]
    temp = abs(userX - RomList[0][0]) + abs(userY - RomList[0][1])
    for i in range(1,len(RomList)):
        if temp>abs(userX - RomList[i][0]) + abs(userY - RomList[i][1]):
            end = RomList[i][1],RomList[i][0]
    return end


def startAstar(file_path):
    ax = []
    ay = []
    data = []
    rpath = []
    maze = read_img(file_path)
    start = UseRomListist(UL)[0][1],UseRomListist(UL)[0][0]
    # start = y,x
    end = Compare()
    # end = y,x
    path = Astar(maze,start,end)
    for i in path:
        rpath.append(tuple(4*elem for elem in i))
    for i in rpath:
        ax.append(i[1])
        ay.append(i[0])
    for i in range(len(ax)):
        data.append({'x':ax[i],'y':ay[i]})
    return (json.dumps(data,ensure_ascii=False,indent='\t'))
if __name__ == '__main__':
    file_path = sys.argv[3]
    print(startAstar(file_path))
