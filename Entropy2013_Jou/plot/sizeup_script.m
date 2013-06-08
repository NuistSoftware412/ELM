function sizeup_script
data;
figure(1);
plot(x2,sizeup4(:,1),'-k*',x2,sizeup6(:,1),'-r+',x2,sizeup8(:,1),'-bo');
xlabel('m');
ylabel('sizeup');
title('Sizeup of Training with 5 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;

figure(2);
plot(x2,sizeup4(:,2),'-k*',x2,sizeup6(:,2),'-r+',x2,sizeup8(:,2),'-bo');
xlabel('m');
ylabel('sizeup');
title('Sizeup of Training with 7 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;

figure(3);
plot(x2,sizeup4(:,3),'-k*',x2,sizeup6(:,3),'-r+',x2,sizeup8(:,3),'-bo');
xlabel('m');
ylabel('sizeup');
title('Sizeup of Training with 9 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;

figure(4);
plot(x2,sizeup4(:,4),'-k*',x2,sizeup6(:,4),'-r+',x2,sizeup8(:,1),'-bo');
xlabel('m');
ylabel('sizeup');
title('Sizeup of Training with 11 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;
