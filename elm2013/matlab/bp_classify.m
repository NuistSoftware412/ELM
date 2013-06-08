function [TrainingAccuracy,TestingAccuracy]=bp_classify(TrainingFile,TestingFile,HiddenNodeNumber)
%for classification only

train_data=load(TrainingFile);
P = train_data(:,2:size(train_data,2))';
T = train_data(:,1)';
%train the single hidden layer network with BP
net = newff(P,T,HiddenNodeNumber);
[net,tr] = train(net,P,T);

%test the training data
s=sim(net,P);
s=floor(s+0.5);
tmp = T - s;

count = 0;
for i=1:size(train_data,1)
    if tmp(i) == 0,
        count=count+1;
    end
end
TrainingAccuracy=count/size(train_data,1);

%test the testing data
test_data = load(TestingFile);
TP = test_data(:,2:size(test_data,2))';
TT = test_data(:,1)';
s=sim(net,TP);
s=floor(s+0.5);
tmp = TT - s;

count = 0;
for i=1:size(test_data,1)
    if tmp(i) == 0,
        count=count+1;
    end
end
TestingAccuracy=count/size(test_data,1);


end
