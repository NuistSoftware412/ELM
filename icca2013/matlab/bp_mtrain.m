
for m=1:5
train_data=load('T0/t0_norm4.data');
P = train_data(:,2:4)';
T = train_data(:,1)';
net = newff(P,T,6);
[net,tr] = train(net,P,T);
tT_data = load('T2/t2_norm_T.data');
tP_data = load('T2/t2_norm_P.data');
TT = tT_data';
TP = tP_data';
s=sim(net,TP);
s=floor(s+0.5);
tmp = TT - s;

count = 0;
for i=1:499
    if tmp(i) == 0,
        count=count+1;
    end
end
result(4,m)=count/499;
end





